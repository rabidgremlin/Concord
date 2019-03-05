import React, {Component} from 'react';
import {Drawer, DrawerContent, DrawerHeader, DrawerTitle, List, ListItem} from 'rmwc';
import {Link} from "react-router-dom";

export default class Menu extends Component {

    constructor(props) {
        super(props);
        this.state = ({
            menuOpen: false,
            data: {}
        });
    }

    // updated from parent, allows another component to toggle the menu
    componentWillReceiveProps(nextProps) {
        if (nextProps.menuOpen !== this.state.menuOpen) {
            this.setState({menuOpen: nextProps.menuOpen});
        }
    }

    render() {
        return (
            <Drawer
                modal
                open={this.state.menuOpen}
                onClose={this.props.toggleMenu}
            >
                <DrawerHeader>
                    <DrawerTitle style={{fontSize: '30px'}}>Menu</DrawerTitle>
                </DrawerHeader>
                <DrawerContent>
                    <List>
                        <ListItem style={{fontSize: '20px'}}>
                            <Link to="/labels">Labels</Link>
                        </ListItem>
                        <ListItem style={{fontSize: '20px'}}>
                            <Link to="/stats">Stats</Link>
                        </ListItem>
                    </List>
                </DrawerContent>
            </Drawer>
        )
    }

}