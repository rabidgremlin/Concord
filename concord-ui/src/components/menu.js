import React, { Component } from 'react';
import {
  Drawer,
  DrawerContent,
  DrawerHeader,
  DrawerTitle,
  Icon,
  List,
  MenuItem
} from 'rmwc';
import { Link } from 'react-router-dom';

export default class Menu extends Component {
  constructor(props) {
    super(props);
    this.state = {
      menuOpen: false,
      data: {}
    };
  }

  // updated from parent, allows another component to toggle the menu
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
            <Link to='/labels' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='outlined_flag' /> Labels
              </MenuItem>
            </Link>
            <Link to='/stats' style={{ textDecoration: 'none' }}>
              <MenuItem style={{ paddingLeft: 13 }}>
                <Icon icon='bar_chart' /> Stats
              </MenuItem>
            </Link>
          </List>
        </DrawerContent>
      </Drawer>
    );
  }
}
