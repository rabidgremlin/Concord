import React, {Component} from 'react';
import {Toolbar, ToolbarRow, ToolbarSection, ToolbarTitle, ToolbarIcon} from 'rmwc';
import AppScoresDrawer from './scores_drawer';
import {getTotalUserVotes} from "../api";
import { connect } from 'react-redux'

export class Navbar extends Component {

  constructor(props) {
    super(props);
    this.state = {loading: true, isOpen: false};
    this.toggleDrawer = this.toggleDrawer.bind(this);
  }

  componentDidMount() {
    this.props.dispatch(getTotalUserVotes());
  }

  // componentDidUpdate(oldProps) {
  //   const newProps = this.props;
  //   if(oldProps.loading !== newProps.loading) {
  //     this.setState({loading: newProps.loading})
  //   }
  // }

  toggleDrawer()
  {
      let open = !this.state.isOpen;
      this.setState({isOpen: open})
  }

  render() {
    return (
      <Toolbar style={{borderRadius: '5px'}}>
        <ToolbarRow>
          <ToolbarSection alignStart>
            <ToolbarTitle style={{fontSize: '30px'}}>Concord</ToolbarTitle>
          </ToolbarSection>
          <ToolbarSection alignEnd>
            <ToolbarIcon icon="stars" onClick={this.toggleDrawer}/>
            <AppScoresDrawer isOpen={this.state.isOpen}/>
            <ToolbarIcon icon="exit_to_app" onClick={this.props.logout}/>
          </ToolbarSection>
        </ToolbarRow>
      </Toolbar>
    )
  }

}

export default connect((state) => ({ loading: state.loading, isOpen: state.isOpen }))(Navbar);