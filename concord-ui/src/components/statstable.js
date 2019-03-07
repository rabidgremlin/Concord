import React, { Component } from 'react';

import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHead,
  DataTableHeadCell,
  DataTableRow
} from 'rmwc/DataTable';
import '@rmwc/data-table/data-table.css';
import { getUserStats } from '../api';
import { connect } from 'react-redux';

export class StatsTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statsData: [],
      doneFirstSort: false
    };
  }

  componentDidMount() {
    this.props.dispatch(getUserStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading) {
      this.setState({ statsData: newProps.statsData });
      // this.sortByScore(-1);
    }
  }

  /**
   * Idea behind the score:
   * - Encourage:
   *  - Voting more
   *  - Voting correctly
   *  - Trashing correctly
   * - Discourage:
   *  - Voting less (being slow, not playing etc.)
   *  - Trashing incorrectly
   *  - Skipping incorrectly
   *    - skipping is included in consensus, therefore, if you skip but others don't score goes down
   */
  static computeScore(userStats) {
    if (userStats.totalVotesWithConsensus <= 0) {
      return 0;
    }
    // agreement goes up with trash votes; therefore trashing can both increase and decrease the score
    // trashing has lower weight since only one user gets to vote (else unfair to users that never see the phrase)
    // include total votes in the score (else unfair to users that vote on phrases that don't meet consensus)
    const agreementRating =
      userStats.completedVotes / userStats.totalVotesWithConsensus;
    const score =
      (userStats.totalVotes - userStats.trashVotes / 2) * agreementRating;
    return Math.trunc(score);
  }

  toPercentage = (n, d) => (d > 0 ? 100 * (n / d) : 0).toFixed(2);

  render() {
    if (this.props.loading || !this.props.statsData || !this.state.statsData) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }
    const data = this.state.statsData;
    const dataLength = data.length;
    if (dataLength <= 0) {
      return (
        <div>
          <p>No stats to display</p>
        </div>
      );
    }
    if (!this.state.doneFirstSort) {
      this.sortByScore(-1);
      this.setState({ doneFirstSort: true });
    }
    return (
      <DataTable style={{ minHeight: window.innerHeight * 0.9, width: '100%' }}>
        <DataTableContent style={{ fontSize: '20px' }}>
          <DataTableHead>
            <DataTableRow>
              <DataTableHeadCell>User</DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.scoreSortDir || null}
                onSortChange={this.sortByScore}
              >
                Score
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.totalSortDir || null}
                onSortChange={this.sortByTotal}
              >
                Total Phrases
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.completedSortDir || null}
                onSortChange={this.sortByCompleted}
              >
                Completed Phrases
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.trashSortDir || null}
                onSortChange={this.sortByTrashed}
              >
                Trashed Phrases
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.agreementRateSortDir || null}
                onSortChange={this.sortByAgreementRate}
              >
                Agreement Rating
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.trashRateSortDir || null}
                onSortChange={this.sortByTrashedRate}
              >
                Trash Rate
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.agreementRateNoTrashSortDir || null}
                onSortChange={this.sortByAgreementRateNoTrash}
              >
                Agreement Rating
                <br />
                (ignoring trashed phrases)
              </DataTableHeadCell>
              <DataTableHeadCell alignEnd />
            </DataTableRow>
          </DataTableHead>
          <DataTableBody>
            {[...Array(dataLength)].map((v, i) => (
              <DataTableRow key={i} style={{ width: '20%' }}>
                <DataTableCell>{data[i].userId}</DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {StatsTable.computeScore(data[i]).toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {data[i].totalVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {data[i].completedVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {data[i].trashVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(
                    data[i].completedVotes,
                    data[i].totalVotesWithConsensus
                  )}
                  %
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(data[i].trashVotes, data[i].totalVotes)}%
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(
                    data[i].completedVotesIgnoringTrash,
                    data[i].totalVotesWithConsensusIgnoringTrash
                  )}
                  %
                </DataTableCell>
                <DataTableCell alignEnd />
              </DataTableRow>
            ))}
          </DataTableBody>
        </DataTableContent>
      </DataTable>
    );
  }

  clearSorts() {
    this.setState({
      scoreSortDir: null,
      totalSortDir: null,
      totalWithConsensusSortDir: null,
      completedSortDir: null,
      trashSortDir: null,
      agreementRateSortDir: null,
      trashRateSortDir: null,
      agreementRateNoTrashSortDir: null
    });
  }

  /**
   * Sort rows for the given property according to the supplier function
   */
  sortRows = (property, sortDir, supplier) => {
    this.clearSorts();
    this.setState({
      [property]: sortDir,
      statsData: this.state.statsData.sort(
        (a, b) => sortDir * (supplier(a) - supplier(b))
      )
    });
  };

  sortByScore = (sortDir) =>
    this.sortRows('scoreSortDir', sortDir, (a) => StatsTable.computeScore(a));

  sortByTotal = (sortDir) =>
    this.sortRows('totalSortDir', sortDir, (a) => a.totalVotes);

  sortByCompleted = (sortDir) =>
    this.sortRows('completedSortDir', sortDir, (a) => a.completedVotes);

  sortByTrashed = (sortDir) =>
    this.sortRows('trashSortDir', sortDir, (a) => a.trashVotes);

  sortByAgreementRate = (sortDir) =>
    this.sortRows('agreementRateSortDir', sortDir, (a) =>
      this.toPercentage(a.completedVotes, a.totalVotesWithConsensus)
    );

  sortByTrashedRate = (sortDir) =>
    this.sortRows('trashRateSortDir', sortDir, (a) =>
      this.toPercentage(a.trashVotes, a.totalVotes)
    );

  sortByAgreementRateNoTrash = (sortDir) =>
    this.sortRows('agreementRateNoTrashSortDir', sortDir, (a) =>
      this.toPercentage(
        a.completedVotesIgnoringTrash,
        a.totalVotesWithConsensusIgnoringTrash
      )
    );
}

export default connect((state) => ({
  error: state.stats.error,
  loading: state.stats.loading,
  statsData: state.stats.statsData
}))(StatsTable);
