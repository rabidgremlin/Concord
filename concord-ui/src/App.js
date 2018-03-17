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


import { connect} from 'react-redux'
import { killSession } from './actions'



class App extends Component {
  state = { drawer: false, login: false }

  drawerToggle = () => { this.setState({ ...this.state, drawer: !this.state.drawer }); console.log("d toggle"); }
  loginToggle = () => { this.setState({ ...this.state, login: !this.state.login }) }

  logout = () => { this.props.dispatch(killSession())}

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
        <Login />
      )
    } else {
      return (
        <div>
          <Navbar  logout={this.logout} />          
          <Router>
            <Switch>
              <Route exact path="/" component={LabelPhrase} />
            </Switch>
          </Router>
        </div>
      )
    }
  }
}

export default connect((state) => ({ logged_in: state.session.logged_in }))(App);