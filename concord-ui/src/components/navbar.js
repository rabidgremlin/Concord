import React, {Component} from 'react';
import {Toolbar, ToolbarRow, ToolbarSection, ToolbarTitle, ToolbarIcon} from 'rmwc';
import {getTotalUserVotes} from "../api";

export default class Navbar extends Component {

  constructor(props) {
    super(props);
  }

  renderScores() {
    this.props.dispatch(getTotalUserVotes());
    console.log(this.props.userScores)
  }

  render() {
    return (
      <Toolbar style={{borderRadius: '5px'}}>
        <ToolbarRow>
          <ToolbarSection alignStart>           
            <ToolbarTitle style={{fontSize: '30px'}}>Concord</ToolbarTitle>
          </ToolbarSection>
          <ToolbarSection alignEnd>
            <ToolbarIcon icon="stars" onClick={this.renderScores}/>
            <ToolbarIcon icon="power_settings_new" onClick={this.props.logout}/>
          </ToolbarSection>
        </ToolbarRow>
      </Toolbar>
    )
  }

}
