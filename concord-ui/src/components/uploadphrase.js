import React, { Component } from 'react';
import { Button } from '@rmwc/button';
import { TextField } from '@rmwc/textfield';
import { connect } from 'react-redux';
import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHeadCell,
  DataTableRow
} from '@rmwc/data-table';
import '@rmwc/data-table/data-table.css';
import { Dialog, DialogActions, DialogContent, DialogTitle } from '@rmwc/dialog';
import { postUnlabelledPhrases } from '../api';
import { PhraseSplitter } from '../util/phraseSplitter';

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
    this.state = {
      textField: '',
      invalidData: true,
      phrases: [],
      submissionDialogOpen: false
    };
    this.phraseSplitter = new PhraseSplitter('14pt Arial', 0.85);
  }

  componentWillUpdate(nextProps, nextState) {
    nextState.invalidData = !nextState.textField;
  }

  checkPhrases = () => {
    let uniquePhrases = new Set();
    let phrases = this.state.textField
      .split('\n')
      .map((phrase) => this.cleanText(phrase) || '')
      .filter((phrase) => {
        if (phrase === '' || uniquePhrases.has(phrase)) {
          return false;
        }
        uniquePhrases.add(phrase);
        return true;
      });
    this.setState({ textField: phrases.join('\n'), phrases: phrases });
  };

  cleanText = (s) => s.trim();

  submitPhrases() {
    const unlabelledPhrases = this.state.phrases.map((phrase) => ({
      text: phrase,
      possibleLabel: ''
    }));
    this.props.dispatch(postUnlabelledPhrases(unlabelledPhrases));
  }

  clearAllState = () =>
    this.setState({
      submissionDialogOpen: false,
      textField: '',
      invalidData: true,
      phrases: []
    });

  handleChange = (evt) => this.setState({ ...this.state, textField: evt.target.value });

  render() {
    if (this.state.submissionDialogOpen && this.props.loading) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }

    let dialogTitle;
    let dialogContent;
    let closeDialogFunction;
    if (this.state.submissionDialogOpen && !this.props.loading) {
      const phraseCountFormatted = `${this.state.phrases.length} phrase${this.state.phrases.length !== 1 ? 's' : ''}.`;
      if (!this.props.error) {
        dialogTitle = 'Upload Successful';
        dialogContent = `Successfully uploaded ${phraseCountFormatted}`;
        closeDialogFunction = () => this.clearAllState();
      } else {
        dialogTitle = 'Upload Failed';
        dialogContent = `Failed to upload ${phraseCountFormatted}`;
        // Don't clear the table if the POST request failed (user may want to retry)
        closeDialogFunction = () => this.setState({ submissionDialogOpen: false });
      }
    }
    const SubmissionDialog = () => {
      return (
        <div>
          <Dialog open={this.state.submissionDialogOpen && !this.props.loading}>
            <DialogTitle>{dialogTitle || ''}</DialogTitle>
            <DialogContent>{dialogContent || ''}</DialogContent>
            <DialogActions>
              <Button onClick={closeDialogFunction}>OK</Button>
            </DialogActions>
          </Dialog>
        </div>
      );
    };

    if (this.state.phrases.length > 0) {
      return (
        <div>
          <SubmissionDialog />
          <Button
            style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }}
            raised
            onClick={(evt) => {
              this.submitPhrases();
              this.setState({ submissionDialogOpen: true });
            }}
          >
            Submit
          </Button>
          <Button style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }} raised onClick={() => this.setState({ phrases: [] })}>
            Edit
          </Button>
          <Button style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }} raised onClick={this.clearAllState}>
            Clear
          </Button>
          <DataTable style={{ minHeight: this.state.phrases.length * 20, width: '100%' }}>
            <DataTableContent style={{ fontSize: '14pt' }}>
              <DataTableBody>
                {this.state.phrases.map((phrase, i) => (
                  <DataTableRow key={i}>
                    <DataTableHeadCell style={{ padding: '', margin: '' }}>{i + 1}</DataTableHeadCell>
                    <DataTableCell style={{ width: '100%' }}>
                      <div>
                        {this.phraseSplitter.splitPhrase(phrase).map((line, j) => (
                          <div key={`${i}.${j}`}>{line}</div>
                        ))}
                      </div>
                    </DataTableCell>
                  </DataTableRow>
                ))}
              </DataTableBody>
            </DataTableContent>
          </DataTable>
        </div>
      );
    }
    return (
      <div>
        <SubmissionDialog />
        <Button
          style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }}
          raised
          onClick={this.checkPhrases}
          disabled={this.state.invalidData}
        >
          Check
        </Button>
        <TextField
          style={{ minHeight: window.innerHeight, width: '100%' }}
          label='Enter phrases, each on a new line'
          textarea
          outlined
          fullwidth
          value={this.state.textField}
          onChange={this.handleChange}
        />
      </div>
    );
  }
}

export default connect((state) => ({
  error: state.uploadPhrase.error,
  loading: state.uploadPhrase.loading
}))(UploadPhrase);
