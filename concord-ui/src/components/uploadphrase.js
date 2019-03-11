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
        <Button raised onClick={this.submitTextField}>Submit</Button>
        <TextField
          textarea
          outlined
          fullwidth
          label='Enter phrases, each on a new line'
          characterCount
          helpText={{
            children: 'The field is required'
          }}
        />
      </div>
    );
  }
}
