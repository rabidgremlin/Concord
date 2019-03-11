import React, { Component } from 'react';
import { TextField, Button } from 'rmwc';
import {connect} from "react-redux";
import {postPhrases} from "../api";

export class UploadPhrase extends Component {
  constructor(props) {
    super(props);
    this.state = {textField: ''}
  }

  submitTextField = () => {
    console.log('submitting');
    console.log(this.state.textField);
    // this.props.dispatch(postPhrases([{text: 'hi hi', possibleLabel: 'bye'}, {text: 'hello', possibleLabel: 'bye'}]));
  };

  handleChange = (val) => (evt) => {
    this.setState({ ...this.state, [val]: evt.target.value });
  };

  render() {
    return (
      <div>
        <Button raised onClick={this.submitTextField}>Submit</Button>
        <TextField
          label='Enter phrases, each on a new line'
          textarea
          outlined
          fullwidth
          required
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
