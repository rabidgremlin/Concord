import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch
} from 'react-router-dom';
import Navbar from './components/navbar';
import Login from './components/login';
import Menu from './components/menu';
import LabelPhrase from './components/labelphrase';
import { SimpleDialog } from 'rmwc';
import { ThemeProvider } from '@rmwc/theme';
import { connect } from 'react-redux';
import { killSession, resetError } from './actions';
import Statstable from './components/statstable';

export class App extends Component {
  constructor(props) {
    super(props);
    this.state = { menuOpen: false };
  }

  logout = () => this.props.dispatch(killSession());

  toggleMenu = () => this.setState({ menuOpen: !this.state.menuOpen });

  render() {
    if (!this.props.logged_in) {
      return (
        <ThemeProvider
          options={{
            primary: '#3f51b5',
            secondary: 'black'
          }}
        >
          <Login />
          <SimpleDialog
            title='Error'
            body={this.props.errorMsg}
            open={this.props.hasError}
            cancelLabel={null}
            onClose={(evt) => {
              this.props.dispatch(resetError());
            }}
          />
        </ThemeProvider>
      );
    } else {
      return (
        <ThemeProvider
          options={{
            primary: '#3f51b5',
            secondary: 'black'
          }}
        >
          <Router basename={process.env.PUBLIC_URL}>
            <div>
              <Menu
                menuOpen={this.state.menuOpen}
                toggleMenu={this.toggleMenu}
              />
              <Navbar logout={this.logout} toggleMenu={this.toggleMenu} />
              <SimpleDialog
                title='Error'
                body={this.props.errorMsg}
                open={this.props.hasError}
                cancelLabel={null}
                onClose={(evt) => {
                  this.props.dispatch(resetError());
                }}
              />
              <Switch>
                <Route exact path='/labels' component={LabelPhrase} />
                <Route exact path='/stats' component={Statstable} />
              </Switch>
              <Redirect from='/' to='/labels' />
            </div>
          </Router>
        </ThemeProvider>
      );
    }
  }
}

export default connect((state) => ({
  logged_in: state.session.logged_in,
  hasError: state.error.hasError,
  errorMsg: state.error.msg
}))(App);
