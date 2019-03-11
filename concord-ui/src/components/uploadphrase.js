import React, { Component } from 'react';
import { TextField, Button } from 'rmwc';

export default class UploadPhrase extends Component {
  constructor(props) {
    super(props);
  }

  submitTextField = () => {
    console.log('submitting');
  };

  render() {
    return (
      <div>
        <p>Enter phrases, each on a new line</p>
        <TextField
          textarea
          outlined
          fullwidth
          label='textarea...'
          rows='8'
          maxLength={20}
          characterCount
          helpText={{
            persistent: true,
            validationMsg: true,
            children: 'The field is required'
          }}
        />
        <Button label='Submit' raised onClick={this.submitTextField} />
      </div>
    );
  }
}
