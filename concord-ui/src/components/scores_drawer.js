import {Drawer, DrawerContent, DrawerHeader, DrawerSubtitle, DrawerTitle, List, ListItem} from 'rmwc';

import React, {Component} from 'react';

export default class AppScoresDrawer extends Component {

    constructor(props) {
        super(props);
        this.state = {isOpen: false}
    }

    handleChange = () => {
        if (!this.state.isOpen)
        {
            this.setState({isOpen: true});
        }
        else
        {
            this.setState({isOpen: false});
        }
    };

    componentDidUpdate(oldProps) {
        const newProps = this.props;
        if (oldProps.isOpen !== newProps.isOpen)
        {
            this.setState({isOpen: newProps.isOpen})
        }
    }

    render() {
        console.log("IS OPEN" + this.props.isOpen);
        if (this.state.isOpen)
        {
            return (
                <Drawer>
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
            )
        }   else {
            return <div>closed</div>
        }
    }

}