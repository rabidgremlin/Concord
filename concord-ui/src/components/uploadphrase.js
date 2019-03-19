import React, { Component } from 'react';
import { Button } from '@rmwc/button';
import { TextField } from '@rmwc/textfield';
import { connect } from 'react-redux';
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
import { Dialog, DialogActions, DialogButton, DialogContent, DialogTitle } from '@rmwc/dialog';
import { postUnlabelledPhrases } from '../api';

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reloadApiData: false,
      textField: '',
      invalidData: true,
      phrases: [],
      submissionDialogOpen: false
    };
  }

  componentWillUpdate(nextProps, nextState) {
    nextState.invalidData = !nextState.textField;
  }

  async componentWillReceiveProps(nextProps) {
    if (nextProps.reloadApiData !== this.state.reloadApiData) {
      this.setState({
        reloadApiData: nextProps.reloadApiData
      });
      // refresh the page
      this.clearFields();
    }
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
    this.setState({ textField: '', invalidData: true, phrases: phrases });
  };

  cleanText = (s) => s.trim().toLowerCase();

  submitPhrases() {
    const unlabelledPhrases = this.state.phrases.map((phrase) => ({
      text: phrase,
      possibleLabel: ''
    }));
    this.props.dispatch(postUnlabelledPhrases(unlabelledPhrases));
    this.setState({ submissionDialogOpen: true });
  }

  clearFields = () =>
    this.setState({
      textField: '',
      invalidData: true,
      phrases: []
    });

  handleChange = (evt) => this.setState({ ...this.state, textField: evt.target.value });

  closeDialog = () => {
    this.setState({ submissionDialogOpen: false });
    this.clearFields();
  };

  render() {
    const SubmissionDialog = () => {
      if (!this.props.error) {
        return (
          <div>
            <Dialog open={this.state.submissionDialogOpen} onClose={() => this.closeDialog()}>
              <DialogTitle>Upload Successful</DialogTitle>
              <DialogContent>Uploaded {this.state.phrases.length} phrases.</DialogContent>
              <DialogActions>
                <DialogButton action='close' isDefaultAction>
                  OK
                </DialogButton>
              </DialogActions>
            </Dialog>
          </div>
        );
      } else {
        return (
          <div>
            <Dialog open={this.state.submissionDialogOpen} onClose={() => this.closeDialog()}>
              <DialogTitle>Upload Failure</DialogTitle>
              <DialogContent>Failed to upload {this.state.phrases.length} phrases.</DialogContent>
              <DialogActions>
                <DialogButton action='close' isDefaultAction>
                  OK
                </DialogButton>
              </DialogActions>
            </Dialog>
          </div>
        );
      }
    };
    this.props.enableRefresh();

    if (this.state.phrases.length > 0) {
      return (
        <div>
          <SubmissionDialog />
          <Button style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }} raised onClick={this.submitPhrases}>
            Submit
          </Button>
          <Button style={{ margin: '0.5rem 0.5rem 0.5rem 0rem' }} raised onClick={this.clearFields}>
            Retry
          </Button>
          <DataTable style={{ minHeight: this.state.phrases.length * 20, width: '100%' }}>
            <DataTableContent style={{ fontSize: '10pt' }}>
              <DataTableHead>
                <DataTableRow>
                  <DataTableHeadCell>Phrase</DataTableHeadCell>
                </DataTableRow>
              </DataTableHead>
              <DataTableBody>
                {this.state.phrases.map((phrase) => (
                  <DataTableRow key={phrase}>
                    <DataTableCell>{phrase}</DataTableCell>
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
