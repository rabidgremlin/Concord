import React, {Component} from 'react';
import {Toolbar, ToolbarRow, ToolbarSection, ToolbarMenuIcon, ToolbarTitle, ToolbarIcon} from 'rmwc';

export default class Navbar extends Component {
  render() {
    return (
      <Toolbar>
        <ToolbarRow>
          <ToolbarSection alignStart>           
            <ToolbarTitle>Concord</ToolbarTitle>
          </ToolbarSection>
          <ToolbarSection alignEnd>
            <ToolbarIcon icon="power_settings_new" onClick={this.props.logout}/>
          </ToolbarSection>
        </ToolbarRow>
      </Toolbar>
    )
  }
}