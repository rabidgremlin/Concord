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
    this.state = {};
  }

  componentDidMount() {
    this.props.dispatch(getSystemStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading) {
      this.setState({ statsData: newProps.statsData });
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
    const data = this.state.statsData;
    const dataLength = Object.keys(data).length;
    if (dataLength <= 0) {
      return (
        <div>
          <p>No stats to display</p>
        </div>
      );
    }
    return (
      <DataTable style={{ minHeight: dataLength * 20, width: '100%' }}>
        <DataTableContent style={{ fontSize: '20px' }}>
          <DataTableHead>
            <DataTableRow>
              <DataTableHeadCell alignEnd>Phrases</DataTableHeadCell>
              <DataTableHeadCell alignEnd>Completed Phrases</DataTableHeadCell>
              <DataTableHeadCell alignEnd>
                Phrases With Consensus
              </DataTableHeadCell>
              <DataTableHeadCell alignEnd>
                Phrases With Consensus
                <br /> (not completed)
              </DataTableHeadCell>
              <DataTableHeadCell alignEnd>Labels</DataTableHeadCell>
              <DataTableHeadCell alignEnd>Labels Used</DataTableHeadCell>
              <DataTableHeadCell alignEnd>Votes</DataTableHeadCell>
              <DataTableHeadCell alignEnd>Users</DataTableHeadCell>
              <DataTableHeadCell alignEnd />
            </DataTableRow>
          </DataTableHead>
          <DataTableBody>
            <DataTableRow>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.totalPhrases.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.completedPhrases.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.phrasesWithConsensus.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.phrasesWithConsensusNotCompleted.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.totalLabels.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.labelsUsed.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.totalVotes.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd style={{ width: '10%' }}>
                {data.userCount.toLocaleString()}
              </DataTableCell>
              <DataTableCell alignEnd />
            </DataTableRow>
          </DataTableBody>
        </DataTableContent>
      </DataTable>
    );
  }
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData
}))(SystemStatsTable);
