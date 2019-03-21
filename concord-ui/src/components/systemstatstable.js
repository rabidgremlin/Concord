import React, { Component } from 'react';
import { deleteVotesForPhrase, getSystemStats, resolveForPhraseLabel } from '../api';
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
import { connect } from 'react-redux';
import '@rmwc/list/collapsible-list.css';
import { Button } from '@rmwc/button';
import { Typography } from '@rmwc/typography';
import { PhraseSplitter } from '../util/phraseSplitter';

export class SystemStatsTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statsData: {},
      reloadApiData: false
    };
    // for formatting the data table
    this.phraseSplitter = new PhraseSplitter('14pt Arial', 0.4);
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

  async componentWillReceiveProps(nextProps) {
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
    if (this.props.loading) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }

    if (!this.props.statsData || !this.state.statsData || Object.keys(this.state.statsData).length <= 0) {
      return (
        <div>
          <p>No stats to display</p>
        </div>
      );
    }

    this.props.enableRefresh();

    const data = this.state.statsData;
    const deadLockedPhrases = data.deadLockedPhrases;

    const SystemStatsTable = () => {
      return (
        <div>
          <Typography style={{ fontSize: '30px' }} use='subtitle1' tag='h2'>
            System Stats
          </Typography>
          <DataTable style={{ width: '100%' }}>
            <DataTableContent style={{ fontSize: '14pt' }}>
              <DataTableHead>
                <DataTableRow>
                  <DataTableHeadCell alignEnd>Phrases</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Completed Phrases</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Labels</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Labels Used</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Votes</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Users</DataTableHeadCell>
                  <DataTableHeadCell alignEnd>Consensus Level</DataTableHeadCell>
                  <DataTableHeadCell />
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
                  <DataTableCell alignEnd style={{ width: '10%' }}>
                    {data.consensusLevel.toLocaleString()}
                  </DataTableCell>
                </DataTableRow>
              </DataTableBody>
            </DataTableContent>
          </DataTable>
        </div>
      );
    };

    if (this.props.loadingResolvePhrase) {
      return (
        <div>
          <SystemStatsTable />
          <div>
            <p>loading...</p>
          </div>
        </div>
      );
    }
    return (
      <div>
        <SystemStatsTable />
        <Typography style={{ fontSize: '30px' }} use='subtitle1' tag='h2'>
          {deadLockedPhrases.length} Deadlocked Phrases
        </Typography>
        <DataTable style={{ minWidth: '100%' }}>
          <DataTableContent style={{ fontSize: '14pt' }}>
            <DataTableBody>
              {[...Array(Math.min(20, deadLockedPhrases.length))].map((v, i) => (
                <DataTableRow key={i}>
                  <DataTableCell style={{ width: '50%' }}>
                    <div>
                      {this.phraseSplitter.splitPhrase(deadLockedPhrases[i].phrase.text).map((line, j) => (
                        <div key={`${i}.${j}`}>{line}</div>
                      ))}
                    </div>
                  </DataTableCell>
                  <DataTableCell style={{ width: '20%' }}>
                    {deadLockedPhrases[i].labelsInVoteOrder.map((label, j) => (
                      <div key={`${i}.${j}`}>
                        <Button
                          unelevated
                          style={{
                            backgroundColor: '#E0E0E0',
                            color: 'black',
                            marginBottom: '0.5rem',
                            textTransform: 'capitalize'
                          }}
                          onClick={() => this.resolvePhrase(deadLockedPhrases[i].phrase.phraseId, label.label)}
                        >
                          {label.label} ({label.count})
                        </Button>
                      </div>
                    ))}
                  </DataTableCell>
                  <DataTableCell style={{ width: '10%' }}>
                    <div>
                      <Button
                        unelevated
                        style={{
                          backgroundColor: '#E0E0E0',
                          color: 'black',
                          marginBottom: '0.5rem'
                        }}
                        onClick={() => this.clearVotes(deadLockedPhrases[i].phrase.phraseId)}
                      >
                        Clear Votes
                      </Button>
                    </div>
                    <div>
                      <Button
                        unelevated
                        style={{
                          backgroundColor: '#E0E0E0',
                          color: 'black',
                          marginBottom: '0.5rem'
                        }}
                        onClick={() => this.resolvePhrase(deadLockedPhrases[i].phrase.phraseId, 'TRASH')}
                      >
                        Trash
                      </Button>
                    </div>
                    <div>
                      <Button
                        unelevated
                        style={{
                          backgroundColor: '#E0E0E0',
                          color: 'black',
                          marginBottom: '0.5rem'
                        }}
                        onClick={() => this.resolvePhrase(deadLockedPhrases[i].phrase.phraseId, 'SKIPPED')}
                      >
                        Skip
                      </Button>
                    </div>
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

  resolvePhrase(phraseId, label) {
    // remove the phrase from the deadlocked list here (to save an expensive API call)
    // for up to date information (e.g. if multiple users are resolving phrases) use the refresh button
    this.removeDeadLockedPhrase(phraseId);
    this.props.dispatch(resolveForPhraseLabel(phraseId, label));
  }

  removeDeadLockedPhrase(phraseId) {
    let statsData = this.state.statsData;
    statsData.deadLockedPhrases = statsData.deadLockedPhrases.filter((p) => p.phrase.phraseId !== phraseId);
    this.setState({ statsData: statsData });
  }

  clearVotes(phraseId) {
    this.removeDeadLockedPhrase(phraseId);
    this.props.dispatch(deleteVotesForPhrase(phraseId));
  }
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData,
  loadingResolvePhrase: state.nextPhrase.loading,
  errorResolvePhrase: state.nextPhrase.error
}))(SystemStatsTable);
