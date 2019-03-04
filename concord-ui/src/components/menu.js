import React, {Component} from 'react';
import {Drawer, DrawerContent, DrawerHeader, DrawerSubtitle, DrawerTitle, List, ListItem} from 'rmwc';

export default class Menu extends Component {

    constructor(props) {
        super(props);
        this.state = ({menuOpen: false});
    }

    // updated from parent, allows another component to toggle the menu
    componentWillReceiveProps(nextProps) {
        if (nextProps.menuOpen !== this.state.menuOpen) {
            this.setState({menuOpen: nextProps.menuOpen});
        }
    }

    render() {
        return (
            <div>
                <Drawer
                    modal
                    open={this.state.menuOpen}
                    onClose={this.props.toggleMenu}
                >
                    <DrawerHeader>
                        <DrawerTitle>DrawerHeader</DrawerTitle>
                        <DrawerSubtitle>Subtitle</DrawerSubtitle>
                    </DrawerHeader>
                    <DrawerContent>
                        <List>
                            <ListItem>Cookies</ListItem>
                            <ListItem>Pizza</ListItem>
                            <ListItem>Icecream</ListItem>
                        </List>
                    </DrawerContent>
                </Drawer>
            </div>
        )
    }

}