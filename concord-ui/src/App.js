import React, { Component } from 'react'

//import Feed from './feed_basic'

import {
  BrowserRouter as Router,
  Route,
  Link,
  Switch,
  Redirect
} from 'react-router-dom'

import Navbar from './navbar'
import AppDrawer from './drawer'
import Login from './login'
import LabelPhrase from './labelphrase'
import { SimpleDialog } from 'rmwc/Dialog';


import { connect } from 'react-redux'
import { killSession } from './actions'



class App extends Component {
  state = { drawer: false, login: false }

  drawerToggle = () => { this.setState({ ...this.state, drawer: !this.state.drawer }); console.log("d toggle"); }
  loginToggle = () => { this.setState({ ...this.state, login: !this.state.login }) }

  logout = () => { this.props.dispatch(killSession()) }

  /* render() {
     return (
       <div>
        <Navbar toggle={this.drawerToggle} login={this.loginToggle}/>
        <Login opened={this.state.login} toggle={this.loginToggle}/>
        <AppDrawer opened={this.state.drawer}/>
                 
           <p>test</p>
        </div>
     );
   }*/


  render() {
    console.log(this.props.logged_in);
    if (!this.props.logged_in) {
      return (
        <div>
          <Login />
          <SimpleDialog
            title="Error"
            body={this.props.errorMsg}
            open={this.props.hasError}
            cancelLabel={null}
          />
        </div>
      )
    } else {
      return (
        <div>
          <Navbar logout={this.logout} />
          <Router basename={process.env.PUBLIC_URL}>
            <Switch>
              <Route exact path="/" component={LabelPhrase} />
            </Switch>
          </Router>
          <SimpleDialog
            title="Error"
            body={this.props.errorMsg}
            open={this.props.hasError}
            cancelLabel={null}
          />
        </div>
      )
    }
  }
}

export default connect((state) => ({ logged_in: state.session.logged_in, hasError: state.error.hasError, errorMsg: state.error.msg }))(App);

//onClose={evt => this.setState({simpleDialogIsOpen: false})}
//onAccept={evt => console.log('Accepted')}
//onCancel={evt => console.log('Cancelled')}