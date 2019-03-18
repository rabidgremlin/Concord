import React, { Component } from 'react';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import Navbar from './components/navbar';
import Login from './components/login';
import Menu from './components/menu';
import LabelPhrase from './components/labelphrase';
import { SimpleDialog } from '@rmwc/dialog';
import { ThemeProvider } from '@rmwc/theme';
import { connect } from 'react-redux';
import { killSession, resetError } from './actions';
import UserStatsTable from './components/userstatstable';
import SystemStatsTable from './components/systemstatstable';
import UploadPhrase from "./components/uploadphrase";

export class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      menuOpen: false,
      reloadApiData: false,
      refreshEnabled: false
    };
  }

  logout = () => this.props.dispatch(killSession());

  toggleMenu = () => this.setState({ menuOpen: !this.state.menuOpen });

  enableRefresh = () => {
    if (!this.state.refreshEnabled) {
      this.setState({ refreshEnabled: true });
    }
  };

  refreshChildComponent = () => {
    if (this.state.refreshEnabled) {
      this.setState({
        refreshEnabled: false,
        // trigger active child component to refresh
        reloadApiData: !this.state.reloadApiData
      });
    }
  };

  render() {
    const LabelPage = (props) => {
      return <LabelPhrase reloadApiData={this.state.reloadApiData} enableRefresh={this.enableRefresh} {...props} />;
    };
    const UserStatsPage = (props) => {
      return <UserStatsTable reloadApiData={this.state.reloadApiData} enableRefresh={this.enableRefresh} {...props} />;
    };
    const SystemStatsPage = (props) => {
      return (
        <SystemStatsTable reloadApiData={this.state.reloadApiData} enableRefresh={this.enableRefresh} {...props} />
      );
    };
    const UploadPhrasesPage = (props) => {
      return (
        <UploadPhrase reloadApiData={this.state.reloadApiData} enableRefresh={this.enableRefresh} {...props} />
      );
    };

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
              <Menu menuOpen={this.state.menuOpen} toggleMenu={this.toggleMenu} />
              <Navbar
                logout={this.logout}
                toggleMenu={this.toggleMenu}
                refreshChildComponent={this.refreshChildComponent}
              />
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
                <Route exact path='/phrases/vote' render={LabelPage} />
                <Route exact path='/phrases/upload' component={UploadPhrasesPage} />
                <Route exact path='/stats' render={UserStatsPage} />
                <Route exact path='/admin' render={SystemStatsPage} />
              </Switch>
              <Redirect exact from='/' to='/phrases/vote' />
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
