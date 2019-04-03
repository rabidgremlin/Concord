import React, { Component } from 'react';

import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHead,
  DataTableHeadCell,
  DataTableRow
} from '@rmwc/data-table';
import '@rmwc/data-table/data-table.css';
import { getUserStats } from '../api';
import { connect } from 'react-redux';

export class UserStatsTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statsData: [],
      doneFirstSort: false,
      reloadApiData: false
    };
  }

  componentDidMount() {
    this.props.dispatch(getUserStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading) {
      this.setState({
        statsData: newProps.statsData
      });
    }
  }

  async componentWillReceiveProps(nextProps) {
    if (nextProps.reloadApiData !== this.state.reloadApiData) {
      this.setState({
        reloadApiData: nextProps.reloadApiData,
        statsData: [],
        doneFirstSort: false
      });
      // refresh the API data
      this.props.dispatch(getUserStats());
    }
  }

  /**
   * Score = total_votes * agreement_rating, except trash_votes are weighted lower.
   *
   * Rationale:
   * - Encourage:
   *  - Voting more (include total_votes)
   *    - NOT total_votes_with_consensus (else unfair to users that vote on phrases that don't meet consensus)
   *  - Voting correctly (include agreement_rating)
   *  - Trashing correctly (include agreement_rating NOT agreement_rating_no_trash, else BULK_UPLOAD wins)
   * - Discourage:
   *  - Not voting (being slow, not playing etc.)
   *  - Spamming trash/skip
   *    - trashing has lower weight since only one user gets to vote (else unfair to users that never see the phrase)
   *    - skipping is included in consensus, therefore, if you skip but others don't the agreement rating goes down
   *      - (no special behaviour needs to be applied to skip, unless we change to a true skip (abstain))
   */
  computeScore = (userStats) => {
    if (userStats.totalVotesWithConsensus <= 0) {
      return 0;
    }
    const agreementRating = userStats.completedVotes / userStats.totalVotesWithConsensus;
    // const agreementRatingNoTrash = userStats.completedVotesIgnoringTrash / userStats.totalVotesWithConsensusIgnoringTrash;
    // trash votes have 1/3 the weight, but trashing also improves agreement rating...
    const score = (userStats.totalVotes - (userStats.trashVotes * 2) / 3) * agreementRating;
    return Math.trunc(score);
  };

  toPercentage = (n, d) => (d > 0 ? 100 * (n / d) : 0).toFixed(2);

  render() {
    if (this.props.loading) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }

    if (!this.props.statsData || !this.state.statsData || this.state.statsData.length <= 0) {
      return (
        <div>
          <p>No stats to display</p>
        </div>
      );
    }

    if (!this.state.doneFirstSort) {
      this.sortByScore(-1);
      this.setState({
        doneFirstSort: true
      });
    }

    this.props.enableRefresh();

    return (
      <DataTable style={{ minHeight: this.state.statsData.length * 20, width: '100%' }}>
        <DataTableContent style={{ fontSize: '14pt' }}>
          <DataTableHead>
            <DataTableRow>
              <DataTableHeadCell>User</DataTableHeadCell>
              <DataTableHeadCell alignEnd sort={this.state.scoreSortDir || null} onSortChange={this.sortByScore}>
                Score
              </DataTableHeadCell>
              <DataTableHeadCell
                alignEnd
                sort={this.state.efficiencySortDir || null}
                onSortChange={this.sortByEfficiency}
              >
                Efficiency
              </DataTableHeadCell>
              <DataTableHeadCell alignEnd sort={this.state.totalSortDir || null} onSortChange={this.sortByTotal}>
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
            {this.state.statsData.map((userData, i) => (
              <DataTableRow key={i}>
                <DataTableCell>{userData.userId}</DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.computeScore(userData).toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(this.computeScore(userData), userData.totalVotes).toLocaleString()}%
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {userData.totalVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {userData.completedVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(userData.completedVotes, userData.totalVotesWithConsensus)}%
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(userData.trashVotes, userData.totalVotes)}%
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.toPercentage(
                    userData.completedVotesIgnoringTrash,
                    userData.totalVotesWithConsensusIgnoringTrash
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

  clearSorts = () => {
    this.setState({
      scoreSortDir: null,
      totalSortDir: null,
      totalWithConsensusSortDir: null,
      completedSortDir: null,
      trashSortDir: null,
      agreementRateSortDir: null,
      trashRateSortDir: null,
      agreementRateNoTrashSortDir: null,
      efficiencySortDir: null
    });
  };

  /**
   * Sort rows for the given property according to the supplier function
   */
  sortRows = (property, sortDir, supplier) => {
    this.clearSorts();
    this.setState({
      [property]: sortDir,
      statsData: this.state.statsData.sort((a, b) => sortDir * (supplier(a) - supplier(b)))
    });
  };

  sortByScore = (sortDir) => this.sortRows('scoreSortDir', sortDir, (a) => this.computeScore(a));

  sortByEfficiency = (sortDir) =>
    this.sortRows('efficiencySortDir', sortDir, (a) => this.toPercentage(this.computeScore(a), a.totalVotes));

  sortByTotal = (sortDir) => this.sortRows('totalSortDir', sortDir, (a) => a.totalVotes);

  sortByCompleted = (sortDir) => this.sortRows('completedSortDir', sortDir, (a) => a.completedVotes);

  sortByAgreementRate = (sortDir) =>
    this.sortRows('agreementRateSortDir', sortDir, (a) =>
      this.toPercentage(a.completedVotes, a.totalVotesWithConsensus)
    );

  sortByTrashedRate = (sortDir) =>
    this.sortRows('trashRateSortDir', sortDir, (a) => this.toPercentage(a.trashVotes, a.totalVotes));

  sortByAgreementRateNoTrash = (sortDir) =>
    this.sortRows('agreementRateNoTrashSortDir', sortDir, (a) =>
      this.toPercentage(a.completedVotesIgnoringTrash, a.totalVotesWithConsensusIgnoringTrash)
    );
}

export default connect((state) => ({
  error: state.userStats.error,
  loading: state.userStats.loading,
  statsData: state.userStats.statsData
}))(UserStatsTable);
