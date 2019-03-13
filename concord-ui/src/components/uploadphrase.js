import React, {Component} from 'react';
import {Button, TextField} from 'rmwc';
import {connect} from 'react-redux';
import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHead,
  DataTableHeadCell,
  DataTableRow
} from 'rmwc/DataTable';
import {Dialog, DialogActions, DialogButton, DialogContent, DialogTitle} from '@rmwc/dialog';
import {getAllLabels, postPhrases} from '../api';
import '@rmwc/data-table/data-table.css';

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
    this.state = { textField: '', invalidData: true, phrases: [], invalidLabels: new Set() };
  }

  componentDidMount() {
    this.props.dispatch(getAllLabels());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loadingLabels !== newProps.loadingLabels && newProps.labelData) {
      this.setState({
        labels: new Set(newProps.labelData.map((l) => l.label))
      });
    }
  }

  componentWillUpdate(nextProps, nextState) {
    nextState.invalidData = !nextState.textField;
  }

  handleChange = (val) => (evt) => {
    this.setState({ ...this.state, [val]: evt.target.value });
  };

  preprocessPhrases = () => {
    let invalidLabels = new Set();
    let uniquePhrases = new Set();
    let phrases = this.state.textField
      .split('\n')
      .map((textPossibleLabel) => {
        let [text, label] = textPossibleLabel.split(',');
        return {
          text: this.cleanText(text) || '',
          possibleLabel: (label || '').trim()
        };
      })
      .filter((phrase) => {
        // filter out invalid labels and empty phrases
        if (!(phrase.possibleLabel === '' || this.state.labels.has(phrase.possibleLabel))) {
          invalidLabels.add(phrase.possibleLabel);
          return false;
        }
        return phrase.text !== '';
      })
      .filter((phrase) => {
        if (uniquePhrases.has(phrase.text)) {
          return false;
        }
        uniquePhrases.add(phrase.text);
        return true;
      });
    this.setState({ textField: '', invalidData: true, phrases: phrases, invalidLabels: invalidLabels });
  };

  cleanText = (s) => s.trim().toLowerCase();

  submitPhrases = () => {
    this.props.dispatch(postPhrases(this.state.phrases));
    this.clearFields();
  };

  clearFields = () => {
    this.setState({ textField: '', invalidData: true, phrases: [], invalidLabels: new Set() });
  };

  render() {
    if (this.props.loadingLabels || !this.state.labels) {
      return (
        <div>
          <p>loading.....</p>
        </div>
      );
    }
    const InvalidLabelWarning = () => {
      return (
        <Dialog open={this.state.invalidLabels.size > 0} onClose={() => this.setState({ invalidLabels: new Set() })}>
          <DialogTitle>Warning: Invalid labels removed</DialogTitle>
          <DialogContent>{[...this.state.invalidLabels].join(', ')}</DialogContent>
          <DialogActions>
            <DialogButton action='accept' isDefaultAction>
              Ok
            </DialogButton>
          </DialogActions>
        </Dialog>
      );
    };
    if (this.state.phrases.length > 0) {
      return (
        <div>
          <InvalidLabelWarning />
          <Button raised onClick={this.submitPhrases}>
            Submit
          </Button>
          <Button raised onClick={this.clearFields}>
            Retry
          </Button>
          <DataTable style={{ minHeight: this.state.phrases.length * 20, width: '100%' }}>
            <DataTableContent style={{ fontSize: '12pt' }}>
              <DataTableHead>
                <DataTableRow>
                  <DataTableHeadCell>Possible label</DataTableHeadCell>
                  <DataTableHeadCell>Text</DataTableHeadCell>
                </DataTableRow>
              </DataTableHead>
              <DataTableBody>
                {[...Array(this.state.phrases.length)].map((v, i) => (
                  <DataTableRow key={i}>
                    <DataTableCell>{this.state.phrases[i].possibleLabel}</DataTableCell>
                    <DataTableCell>{this.state.phrases[i].text}</DataTableCell>
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
        <InvalidLabelWarning />
        <Button raised onClick={this.preprocessPhrases} disabled={this.state.invalidData}>
          Check
        </Button>
        <TextField
          style={{ minHeight: window.innerHeight, width: '100%' }}
          label='Enter phrases, each on a new line, in the format: text, possible label'
          textarea
          outlined
          fullwidth
          value={this.state.textField}
          onChange={this.handleChange('textField')}
        />
      </div>
    );
  }
}

export default connect((state) => ({
  error: state.uploadPhrase.error,
  loading: state.uploadPhrase.loading,
  loadingLabels: state.label.loading,
  labelData: state.label.labelData
}))(UploadPhrase);
