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
    const data = this.state.statsData;
    const deadLockedPhrases = data.deadLockedPhrases;

    this.element = document.createElement('canvas');
    this.context = this.element.getContext('2d');
    this.context.font = '10pt Arial';

    return (
      <div>
        <h2>System Stats</h2>
        <DataTable style={{ width: '100%' }}>
          <DataTableContent style={{ fontSize: '12pt' }}>
            <DataTableHead>
              <DataTableRow>
                <DataTableHeadCell alignEnd>Phrases</DataTableHeadCell>
                <DataTableHeadCell alignEnd>Completed Phrases</DataTableHeadCell>
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
              {[...Array(Math.min(20, deadLockedPhrases.length))].map((v, i) => (
                <DataTableRow key={i}>
                  <DataTableCell style={{ width: '50%' }}>
                    <div>
                      {this.splitPhrase(deadLockedPhrases[i].phrase.text).map((line) => (
                        <div>{line}</div>
                      ))}
                    </div>
                  </DataTableCell>
                  <DataTableCell style={{ width: '20%' }}>
                    {deadLockedPhrases[i].labelsInVoteOrder.map((label) => (
                      <div>
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

  // hack to put line breaks in DataTable (while trying not to break words)
  splitPhrase(phrase) {
    const textWidth = this.context.measureText(phrase).width;
    const availableWidth = window.innerWidth / 3;
    const widthRatio = textWidth / availableWidth;
    const maxLineLength = Math.trunc(phrase.length / widthRatio) - 1;

    let words = phrase.split(' ');
    // break any long words up
    let i = 0;
    while (i < words.length) {
      if (words[i].length >= maxLineLength) {
        words.splice(i, 1, this.splitWord(words[i], maxLineLength));
        words = this.flattenArray(words);
      }
      i++;
    }

    let lines = [];
    i = 0;
    while (i < words.length) {
      let line = '';
      while (i < words.length && line.length + words[i].length <= maxLineLength) {
        line += ' ' + words[i];
        i++;
      }
      lines.push(line);
    }
    return lines;
  }

  splitWord = (word, splitLength) => {
    let chunks = [];
    for (let i = 0, charsLength = word.length; i < charsLength; i += splitLength) {
      chunks.push(word.substring(i, i + splitLength));
    }
    return chunks;
  };

  flattenArray = (a) => [].concat.apply([], a);

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
