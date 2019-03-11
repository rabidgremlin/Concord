import React, { Component } from 'react';
import { TextField, Button } from 'rmwc';
import {connect} from "react-redux";
import {postPhrases} from "../api";

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
  }

  submitTextField = () => {
    console.log('submitting');
    this.props.dispatch(postPhrases([{text: 'hi hi', possibleLabel: 'bye'}, {text: 'hello', possibleLabel: 'bye'}]));
  };

  render() {
    return (
      <div>
        <Button raised onClick={this.submitTextField}>Submit</Button>
        <TextField
          textarea
          outlined
          fullwidth
          label='Enter phrases, each on a new line'
          helpText={{
            children: 'The field is required'
          }}
        />
      </div>
    );
  }
}

export default connect((state) => ({
  error: state.uploadPhrase.error,
  loading: state.uploadPhrase.loading,
}))(UploadPhrase);
