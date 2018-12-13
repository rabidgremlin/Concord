import React, { Component } from 'react'
import { getAllLabels } from './api'
import { connect } from 'react-redux'
import { Select } from '@rmwc/select';
import { Button, ButtonIcon } from'@rmwc/button';
import './searchbar.css';

function convertLabelDataToLabels(data)
{
    return data.map(x => x['label'])
}

export class Searchbar extends Component {

    componentDidMount() {
        this.props.dispatch(getAllLabels());
    }


    render() {
        if (this.props.loading) {
            return (
              <div><p>loading.....</p></div>
            )
        } 
        else {
            if (this.props.labelData) {
                let labels = convertLabelDataToLabels(this.props.labelData)
                return (
                    <div className="searchBar">
                    <Select
                    className="labelSelector"
                    onChange={evt => this.setState({value: evt.target.value})}
                    style={{minWidth: '250px'}}
                    label="Other labels"
                    outlined
                    placeholder=""
                    options = {
                        labels
                    }
                    />

                    <Button className='labelBtn' onClick={() => {this.props.makeVote(this.state.value)}} outlined>LABEL PHRASE</Button>

                    </div>
                )
            }
        }
    }
}

export default connect((state) => ({ loading: state.label.loading, labelData: state.label.labelData }))(Searchbar);