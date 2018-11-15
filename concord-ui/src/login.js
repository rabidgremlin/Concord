import React, { Component } from 'react'
import { TextField } from 'rmwc';
import { Grid, GridCell } from 'rmwc/Grid';
import { Card} from 'rmwc/Card';
import { Typography } from 'rmwc/Typography';
import { Button } from 'rmwc/Button';

import { connect } from 'react-redux'
//import { callCreateSession } from './actions'

import { createSession } from './api'

class Login extends Component {
  state = { email: "", password: "", invalidData: true }
  handleChange = (val) => (evt) => { this.setState({ ...this.state, [val]: evt.target.value }) }

  componentWillUpdate(nextProps, nextState) {
    nextState.invalidData = !(nextState.email && nextState.password);
  }

   

  // see https://tylermcginnis.com/react-router-programmatically-navigate/ redirect 

  render() {
    return (
      <div>        
        <Typography use="headline1" tag="h1">Concord</Typography>
        <Grid>
          <GridCell desktop="4" tablet="2" phone="0"></GridCell>
          <GridCell desktop="4" tablet="4" phone="4">          
            <Card>
              <Typography use="subtitle2" tag="div" style={{ padding: '0.5rem 1rem' }} theme="text-secondary-on-background">Login</Typography>

              <div style={{padding: '1rem'}}>
                <TextField label="user name" fullwidth required value={this.state.email} onChange={this.handleChange('email')}/>
                <TextField label="password" fullwidth required value={this.state.password} type="password" onChange={this.handleChange('password')}/>
              </div>

              <Button raised style={{ margin: '1rem 1rem 1rem 1rem' }} disabled={this.state.invalidData} onClick={() => { 
                
                //this.props.history.push('/labelphrase') 
                //alert('hello');
                this.props.dispatch(createSession(this.state.email,this.state.password));
                //callCreateSession(this.state.email,this.state.password);
              
              }}>Login</Button>

            </Card>
          </GridCell>
        </Grid>
      </div>
    )
  }
}

export default connect((state) => ({ logged_in: state.session.logged_in }))(Login);