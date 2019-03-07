import React, { Component } from 'react';
import {
  Drawer,
  DrawerHeader,
  DrawerContent,
  ListItem,
  ListItemText
} from 'rmwc';

export default class AppDrawer extends Component {
  render() {
    console.log(this.props.opened);
    return (
      <Drawer persistent open={this.props.opened}>
        <DrawerHeader>DrawerHeader</DrawerHeader>
        <DrawerContent>
          <ListItem>
            <ListItemText>Cookies</ListItemText>
          </ListItem>
          <ListItem>
            <ListItemText>Pizza</ListItemText>
          </ListItem>
          <ListItem>
            <ListItemText>Icecream</ListItemText>
          </ListItem>
        </DrawerContent>
      </Drawer>
    );
  }
}
