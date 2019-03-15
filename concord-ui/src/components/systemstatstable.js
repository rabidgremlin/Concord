import React, { Component } from 'react';
import { getSystemStats, resolveForPhraseLabel } from '../api';
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
import '@rmwc/list/collapsible-list.css';
import { Button } from 'rmwc';

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
    const data = this.state.statsData;
    const deadLockedPhrases = data.deadLockedPhrases;
    return (
      <div>
        <h2>System Stats</h2>
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
              </DataTableRow>
            </DataTableBody>
          </DataTableContent>
        </DataTable>

        <h2>{deadLockedPhrases.length} Deadlocked Phrases</h2>
        <DataTable style={{ minWidth: '100%' }}>
          <DataTableContent style={{ fontSize: '10pt' }}>
            <DataTableBody>
              {[...Array(10)].map((v, i) => (
                <DataTableRow key={i}>
                  {/*<DataTableCell style={{ width: '50%' }}>{deadLockedPhrases[i].phrase.text}</DataTableCell>*/}
                  <DataTableCell style={{ width: '50%' }}>
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase
                    SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long
                    phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE
                    super long phrase SUPER LONG PHRASE super long phrase SUPER LONG PHRASE super long phrase SUPER LONG
                    PHRASE super long phrase SUPER LONG PHRASE super long phrase{' '}
                  </DataTableCell>
                  <DataTableCell style={{ width: '45%' }}>
                    {[...Array(deadLockedPhrases[i].labelsInVoteOrder.length)].map((v2, j) => (
                      <div>
                        <Button
                          unelevated
                          style={{
                            backgroundColor: '#E0E0E0',
                            color: 'black',
                            marginBottom: '0.5rem',
                            textTransform: 'capitalize'
                          }}
                          onClick={() =>
                            this.resolvePhrase(
                              deadLockedPhrases[i].phrase.phraseId,
                              deadLockedPhrases[i].labelsInVoteOrder[j].label
                            )
                          }
                        >
                          {deadLockedPhrases[i].labelsInVoteOrder[j].label} (
                          {deadLockedPhrases[i].labelsInVoteOrder[j].count})
                        </Button>
                      </div>
                    ))}
                  </DataTableCell>
                  <DataTableCell style={{ width: '5%' }}>
                    <div>
                      <Button
                        unelevated
                        style={{
                          backgroundColor: '#E0E0E0',
                          color: 'black',
                          marginBottom: '0.5rem'
                        }}
                        onClick={() => console.log('CLEAR VOTES')}
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
                        onClick={() => console.log('TRASH')}
                      >
                        Trash
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
    console.log('resolving');
    console.log(phraseId, label);
    // remove the phrase from the deadlocked list here (to save an API call)
    // if the user wants up to date information (e.g. multiple users are resolving phrases) they can press the refresh button
    let statsData = this.state.statsData;
    statsData.deadLockedPhrases = statsData.deadLockedPhrases.filter((p) => p.phrase.phraseId !== phraseId);
    this.setState({ statsData: statsData });
    this.props.dispatch(resolveForPhraseLabel(phraseId, label));
  }
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData,
  loadingResolvePhrase: state.nextPhrase.loading,
  errorResolvePhrase: state.nextPhrase.error
}))(SystemStatsTable);
