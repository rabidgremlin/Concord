import React, {Component} from 'react'
import { TextField} from 'rmwc';

import {
  Dialog,
  DefaultDialogTemplate,
  DialogSurface,
  DialogHeader,
  DialogHeaderTitle,
  DialogBody,
  DialogFooter,
  DialogFooterButton,
  DialogBackdrop
} from 'rmwc/Dialog';

class Login extends Component {
  state = {email: "", password: ""}
  handleChange = (val) => (evt) => { this.setState( {...this.state, [val]: evt.target.value} ) }

  /*loginForm = () => {
    return (
      <div>
        <TextField label="email" fullwidth onChange={this.handleChange('email')}/>
        <TextField label="Password" type="password" fullwidth onChange={this.handleChange('password')}/>
      </div>
    )
    
}*/

  render() {
    return (
      <Dialog
        open={this.props.opened}
        onClose={this.props.toggle}
        title={"Login please!"}
        onAccept={() => { console.log( this.state ) }}        
        >
        <DialogSurface>
      <DialogHeader>
        <DialogHeaderTitle>Dialog Title</DialogHeaderTitle>
      </DialogHeader>
      <DialogBody>This is a standard dialog.
      <TextField label="email" fullwidth onChange={this.handleChange('email')}/>
        <TextField label="Password" type="password" fullwidth onChange={this.handleChange('password')}/>
      </DialogBody>
      <DialogFooter>
          <DialogFooterButton cancel>Cancel</DialogFooterButton>
          <DialogFooterButton accept>Sweet!</DialogFooterButton>
      </DialogFooter>
    </DialogSurface>
    <DialogBackdrop />
    </Dialog>
    )
  }
}

export default Login;