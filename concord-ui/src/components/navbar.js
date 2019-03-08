import React, { Component } from 'react';
import {
  Toolbar,
  ToolbarIcon,
  ToolbarRow,
  ToolbarSection,
  ToolbarTitle
} from 'rmwc';

export default class Navbar extends Component {
  render() {
    return (
      <Toolbar style={{ borderRadius: '5px' }}>
        <ToolbarRow>
          <ToolbarSection alignStart>
            <ToolbarIcon icon='menu' onClick={this.props.toggleMenu} />
            <ToolbarTitle style={{ fontSize: '30px' }}>Concord</ToolbarTitle>
          </ToolbarSection>
          <ToolbarSection alignEnd>
            <ToolbarIcon icon='meeting_room' onClick={this.props.logout} />
          </ToolbarSection>
        </ToolbarRow>
      </Toolbar>
    );
  }
}
