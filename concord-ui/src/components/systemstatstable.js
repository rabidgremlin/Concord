import React, { Component } from 'react';
import { getSystemStats } from '../api';
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
import { connect } from 'react-redux';

export class SystemStatsTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statsData: {},
      labelData: [],
      doneFirstSort: false,
      reloadApiData: false
    };
  }

  componentDidMount() {
    this.props.dispatch(getSystemStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading && newProps.statsData) {
      const data = newProps.statsData;
      this.setState({
        statsData: {
          totalPhrases: data.totalPhrases,
          completedPhrases: data.completedPhrases,
          phrasesWithConsensus: data.phrasesWithConsensus,
          phrasesWithConsensusNotCompleted: data.phrasesWithConsensusNotCompleted,
          totalLabels: data.totalLabels,
          labelsUsed: data.labelsUsed,
          totalVotes: data.totalVotes,
          userCount: data.userCount
        },
        labelData: data.labelCountStats
      });
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.reloadApiData !== this.state.reloadApiData) {
      this.setState({
        reloadApiData: nextProps.reloadApiData,
        statsData: {},
        labelData: [],
        doneFirstSort: false
      });
      // refresh the API data
      this.props.dispatch(getSystemStats());
    }
  }

  render() {
    if (this.props.loading || !this.props.statsData || !this.state.statsData) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }
    if (Object.keys(this.state.statsData).length <= 0) {
      return (
        <div>
          <p>No stats to display</p>
        </div>
      );
    }
    if (!this.state.doneFirstSort) {
      this.sortByLabelVoteCount(-1);
      this.setState({ doneFirstSort: true });
    }
    this.props.enableRefresh();
    return (
      <div>
        <DataTable style={{ width: '100%' }}>
          <DataTableContent style={{ fontSize: '20px' }}>
            <DataTableHead>
              <DataTableRow>
                <DataTableHeadCell alignEnd>Phrases</DataTableHeadCell>
                <DataTableHeadCell alignEnd>Completed Phrases</DataTableHeadCell>
                <DataTableHeadCell>Phrases With Consensus</DataTableHeadCell>
                <DataTableHeadCell alignEnd>
                  Phrases With Consensus
                  <br /> (not completed)
                </DataTableHeadCell>
                <DataTableHeadCell alignEnd>Labels</DataTableHeadCell>
                <DataTableHeadCell alignEnd>Labels Used</DataTableHeadCell>
                <DataTableHeadCell alignEnd>Votes</DataTableHeadCell>
                <DataTableHeadCell alignEnd>Users</DataTableHeadCell>
                <DataTableHeadCell />
              </DataTableRow>
            </DataTableHead>
            <DataTableBody>
              <DataTableRow>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.totalPhrases.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.completedPhrases.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.phrasesWithConsensus.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.phrasesWithConsensusNotCompleted.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.totalLabels.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.labelsUsed.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.totalVotes.toLocaleString()}
                </DataTableCell>
                <DataTableCell alignEnd style={{ width: '10%' }}>
                  {this.state.statsData.userCount.toLocaleString()}
                </DataTableCell>
                <DataTableCell />
              </DataTableRow>
            </DataTableBody>
          </DataTableContent>
        </DataTable>
        <DataTable style={{ width: '100%' }}>
          <DataTableContent style={{ fontSize: '20px' }}>
            <DataTableHead>
              <DataTableRow>
                <DataTableHeadCell>Label</DataTableHeadCell>
                <DataTableHeadCell
                  alignEnd
                  sort={this.state.voteCountSortDir || null}
                  onSortChange={this.sortByLabelVoteCount}
                >
                  Votes
                </DataTableHeadCell>
                <DataTableHeadCell
                  alignEnd
                  sort={this.state.completedCountSortDir || null}
                  onSortChange={this.sortByLabelCompletedCount}
                >
                  Completed
                </DataTableHeadCell>
                <DataTableHeadCell alignEnd />
              </DataTableRow>
            </DataTableHead>
            <DataTableBody>
              {[...Array(this.state.labelData.length)].map((v, i) => (
                <DataTableRow key={i}>
                  <DataTableCell style={{ width: '20%' }}>{this.state.labelData[i].label}</DataTableCell>
                  <DataTableCell alignEnd style={{ width: '10%' }}>
                    {this.state.labelData[i].voteCount.toLocaleString()}
                  </DataTableCell>
                  <DataTableCell alignEnd style={{ width: '10%' }}>
                    {this.state.labelData[i].completedPhraseCount.toLocaleString()}
                  </DataTableCell>
                  <DataTableCell />
                </DataTableRow>
              ))}
            </DataTableBody>
          </DataTableContent>
        </DataTable>
      </div>
    );
  }

  clearSorts = () => {
    this.setState({
      voteCountSortDir: null,
      completedCountSortDir: null
    });
  };

  sortRows = (property, sortDir, supplier) => {
    this.clearSorts();
    this.setState({
      [property]: sortDir,
      labelData: this.state.labelData.sort((a, b) => sortDir * (supplier(a) - supplier(b)))
    });
  };

  sortByLabelVoteCount = (sortDir) => this.sortRows('voteCountSortDir', sortDir, (a) => a.voteCount);

  sortByLabelCompletedCount = (sortDir) =>
    this.sortRows('completedCountSortDir', sortDir, (a) => a.completedPhraseCount);
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData
}))(SystemStatsTable);
