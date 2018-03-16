import React, { Component } from 'react'

//import Feed from './feed_basic'

import {
  BrowserRouter as Router,
  Route,
  Link,
  Switch,
  Redirect
} from 'react-router-dom'

//import Navbar from './navbar'
//import AppDrawer from './drawer'
import Login from './login'
import LabelPhrase from './labelphrase' 


class App extends Component {
  state = { drawer: false, login: false }

  drawerToggle = () => { this.setState( { ...this.state, drawer: !this.state.drawer } ); console.log("d toggle"); }
  loginToggle = () => { this.setState( { ...this.state, login: !this.state.login } ) }
    
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

  

  render(){
    return(
      <Router>
        <Switch>
          <Route exact  path="/" component={Login} />
          <Route path="/labelphrase" component={LabelPhrase} />
        </Switch>
      </Router>
    )
  }
}

export default App;