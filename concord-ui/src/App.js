import React, { Component } from 'react'
import Navbar from './navbar'
import AppDrawer from './drawer'
import Login from './login'
//import Feed from './feed_basic'

class App extends Component {
  state = { drawer: false, login: false }

  drawerToggle = () => { this.setState( { ...this.state, drawer: !this.state.drawer } ); console.log("d toggle"); }
  loginToggle = () => { this.setState( { ...this.state, login: !this.state.login } ) }
    
  render() {
    return (
      <div>
       <Navbar toggle={this.drawerToggle} login={this.loginToggle}/>
       <Login opened={this.state.login} toggle={this.loginToggle}/>
       <AppDrawer opened={this.state.drawer}/>
                
          <p>test</p>
       </div>
    );
  }
}

export default App;