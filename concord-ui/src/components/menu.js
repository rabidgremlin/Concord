import React, {Component} from 'react';
import {Drawer, DrawerContent, DrawerHeader, DrawerSubtitle, DrawerTitle, List, ListItem} from 'rmwc';

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


    renderStats() {
        console.log("Rendering stats");
        fetch('/api/stats')
            .then(results => results.json())
            .then(data => {
                for(var d in data){
                    console.log(data[d])
                }
            })
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
                            <ListItem onClick={this.renderStats}>View stats</ListItem>
                            <ListItem>Pizza</ListItem>
                            <ListItem>Icecream</ListItem>
                        </List>
                    </DrawerContent>
                </Drawer>
            </div>
        )
    }

}