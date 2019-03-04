import {Drawer, DrawerContent, DrawerHeader, DrawerSubtitle, DrawerTitle, List, ListItem} from 'rmwc';

import React, {Component} from 'react';
import {getTotalUserVotes} from "../api";
import { connect } from 'react-redux'

export default class AppScoresDrawer extends Component {

    constructor(props) {
        super(props);
        this.state = {isOpen: false, totalUserVoteCounts: {}}
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
        console.log(this.props);
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

// export connect((state) => ({ isOpen: state.isOpen, totalUserVoteCounts: state.totalUserVoteCounts }))(AppScoresDrawer);