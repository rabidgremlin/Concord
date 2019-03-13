import React, { Component } from 'react';
import { Toolbar, ToolbarIcon, ToolbarRow, ToolbarSection, ToolbarTitle } from '@rmwc/toolbar';

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
            <ToolbarIcon icon='refresh' onClick={this.props.refreshChildComponent} />
            <ToolbarIcon icon='exit_to_app' onClick={this.props.logout} />
          </ToolbarSection>
        </ToolbarRow>
      </Toolbar>
    );
  }
}
