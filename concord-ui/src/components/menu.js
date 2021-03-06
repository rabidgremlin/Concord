import React, { Component } from 'react';
import { Drawer, DrawerContent, DrawerHeader, DrawerTitle } from '@rmwc/drawer';
import { Icon } from '@rmwc/icon';
import { List } from '@rmwc/list';
import { MenuItem } from '@rmwc/menu';
import { Link } from 'react-router-dom';

export default class Menu extends Component {
  constructor(props) {
    super(props);
    this.state = {
      menuOpen: false
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.menuOpen !== this.state.menuOpen) {
      this.setState({ menuOpen: nextProps.menuOpen });
    }
  }

  render() {
    return (
      <Drawer modal open={this.state.menuOpen} onClose={this.props.toggleMenu}>
        <DrawerHeader>
          <DrawerTitle style={{ fontSize: '30px' }}>Concord</DrawerTitle>
        </DrawerHeader>
        <DrawerContent>
          <List>
            <Link to='/phrases/vote' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='outlined_flag' /> Vote
              </MenuItem>
            </Link>
            <Link to='/phrases/upload' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='playlist_add' /> Upload
              </MenuItem>
            </Link>
            <Link to='/scores' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='bar_chart' /> Scores
              </MenuItem>
            </Link>
            <Link to='/admin' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='supervisor_account' /> Admin
              </MenuItem>
            </Link>
          </List>
        </DrawerContent>
      </Drawer>
    );
  }
}
