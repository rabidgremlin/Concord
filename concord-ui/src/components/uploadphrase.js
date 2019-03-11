import React, {Component} from 'react';
import {Button, TextField} from 'rmwc';
import {connect} from "react-redux";
import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHead,
  DataTableHeadCell,
  DataTableRow
} from 'rmwc/DataTable';
import {postPhrases} from "../api";

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
    this.state = {textField: '', invalidData: true, invalidDataTable: true, phrases: []}
  }

  handleChange = (val) => (evt) => {
    this.setState({...this.state, [val]: evt.target.value});
  };

  componentWillUpdate(nextProps, nextState) {
    nextState.invalidTextField = !nextState.textField;
  }

  processTextField = () => {
    console.log('checking');
    console.log(this.state.textField);
    const phrases = this.state.textField.split('\n').map(v => {
      const split = v.split(',');
      return {text: split[0], possibleLabel: split[1] || ''};
    });
    this.setState({textField: '', invalidData: true, phrases: phrases});
  };

  submitPhrases = () => {
    console.log('submitting');
    this.props.dispatch(postPhrases(this.state.phrases))
  };

  clearFields = () => {
    console.log('clearing');
    this.setState({textField: '', invalidData: true, invalidDataTable: true, phrases: []});
  };

  render() {
    if (!this.state.loading && this.state.phrases.length > 0) {
      console.log(this.state.phrases);
      return (
        <div>
          <Button
            raised
            onClick={this.submitPhrases}
          >
            Submit
          </Button>
          <Button
            raised
            onClick={this.clearFields}
          >
            Retry
          </Button>
          <DataTable style={{minHeight: this.state.phrases.length * 20, width: '100%'}}>
            <DataTableContent style={{fontSize: '12pt'}}>
              <DataTableHead>
                <DataTableRow>
                  <DataTableHeadCell>Possible label</DataTableHeadCell>
                  <DataTableHeadCell>Text</DataTableHeadCell>
                </DataTableRow>
              </DataTableHead>
              <DataTableBody>
                {[...Array(this.state.phrases.length)].map((v, i) => (
                  <DataTableRow key={i}>
                    <DataTableCell>
                      {this.state.phrases[i].possibleLabel}
                    </DataTableCell>
                    <DataTableCell>
                      {this.state.phrases[i].text}
                    </DataTableCell>
                  </DataTableRow>
                ))}
              </DataTableBody>
            </DataTableContent>
          </DataTable>
        </div>
      )
    }
    return (
      <div>
        <Button
          raised
          onClick={this.processTextField}
          disabled={this.state.invalidTextField}
        >
          Check
        </Button>
        <TextField style={{minHeight: window.innerHeight, width: '100%'}}
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
}))(UploadPhrase);
