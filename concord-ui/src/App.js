import React, {Component} from 'react'
import {BrowserRouter as Router, Route, Switch,} from 'react-router-dom'
import Navbar from './components/navbar'
import Login from './components/login'
import Menu from './components/menu'
import LabelPhrase from './components/labelphrase'
import {SimpleDialog} from 'rmwc/Dialog';
import {ThemeProvider} from '@rmwc/theme';
import {connect} from 'react-redux'
import {killSession, resetError} from './actions'

export class App extends Component {

    constructor(props){
        super(props);
        this.state = ({menuOpen: false});
    }

    logout = () => this.props.dispatch(killSession());

    toggleMenu = () => this.setState({menuOpen: !this.state.menuOpen});

    render() {
        if (!this.props.logged_in) {
            return (
                <div>
                    <ThemeProvider options={{
                        primary: '#3f51b5',
                        secondary: 'black'
                    }}>
                        <Login/>
                        <SimpleDialog
                            title="Error"
                            body={this.props.errorMsg}
                            open={this.props.hasError}
                            cancelLabel={null}
                            onClose={evt => {
                                this.props.dispatch(resetError())
                            }}
                        />
                    </ThemeProvider>
                </div>
            )
        } else {
            return (
                <div>
                    <Menu menuOpen={this.state.menuOpen} toggleMenu={this.toggleMenu}/>
                    <ThemeProvider options={{
                        primary: '#3f51b5',
                        secondary: 'black'
                    }}>
                        <Navbar logout={this.logout} toggleMenu={this.toggleMenu}/>
                        <Router basename={process.env.PUBLIC_URL}>
                            <Switch>
                                <Route exact path="/" component={LabelPhrase}/>
                            </Switch>
                        </Router>
                        <SimpleDialog
                            title="Error"
                            body={this.props.errorMsg}
                            open={this.props.hasError}
                            cancelLabel={null}
                            onClose={evt => {
                                this.props.dispatch(resetError())
                            }}
                        />
                    </ThemeProvider>
                </div>
            )
        }
    }
}

export default connect((state) => ({
    logged_in: state.session.logged_in,
    hasError: state.error.hasError,
    errorMsg: state.error.msg
}))(App);