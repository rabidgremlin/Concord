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
      reloadApiData: false
    };
  }

  componentDidMount() {
    this.props.dispatch(getSystemStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading && newProps.statsData) {
      this.setState({
        statsData: newProps.statsData
      });
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.reloadApiData !== this.state.reloadApiData) {
      this.setState({
        reloadApiData: nextProps.reloadApiData,
        statsData: {}
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
    this.props.enableRefresh();
    console.log(this.state);
    return (
      <div>
        <DataTable style={{ width: '100%' }}>
          <DataTableContent style={{ fontSize: '12pt' }}>
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
              </DataTableRow>
            </DataTableBody>
          </DataTableContent>
        </DataTable>
        <p>Deadlocked phrases</p>
        <DataTable style={{ width: '100%' }}>
          <DataTableContent style={{ fontSize: '8pt' }}>
            <DataTableHead>
              <DataTableRow>
                <DataTableHeadCell>Top Label</DataTableHeadCell>
                <DataTableHeadCell>Second Top Label</DataTableHeadCell>
                <DataTableHeadCell>Phrase</DataTableHeadCell>
                <DataTableHeadCell />
              </DataTableRow>
            </DataTableHead>
            <DataTableBody>
              {[...Array(this.state.statsData.deadLockedPhrases.length)].map((v, i) => (
                <DataTableRow key={i}>
                  <DataTableCell style={{ width: '20%' }}>
                    {this.state.statsData.deadLockedPhrases[i].topLabel.label} (
                    {this.state.statsData.deadLockedPhrases[i].topLabel.count})
                  </DataTableCell>
                  <DataTableCell style={{ width: '20%' }}>
                    {this.state.statsData.deadLockedPhrases[i].secondTopLabel.label} (
                    {this.state.statsData.deadLockedPhrases[i].secondTopLabel.count})
                  </DataTableCell>
                  <DataTableCell style={{ width: '60%' }}>
                    {this.state.statsData.deadLockedPhrases[i].phrase}
                  </DataTableCell>
                </DataTableRow>
              ))}
            </DataTableBody>
          </DataTableContent>
        </DataTable>
      </div>
    );
  }
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData
}))(SystemStatsTable);
