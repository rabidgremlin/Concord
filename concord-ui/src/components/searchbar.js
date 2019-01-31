import React, { Component } from 'react'
import { getAllLabels } from '../api'
import { connect } from 'react-redux'
import { Button, 
    //ButtonIcon 
} from'@rmwc/button';
import './searchbar.css';
import Select from 'react-select';

//function convertLabelDataToLabels(data)
//{
//    return data.map(x => x['label'])
//}

export class Searchbar extends Component {
    constructor(props) {
        super(props)
        this.state = {
            labels: [
                { label: "Loading labels..."}
            ],
            shortDescription: "",
            longDescription: ""
        };
    }

    componentDidMount() {
        this.props.dispatch(getAllLabels());
    }

    componentDidUpdate(oldProps) {
        const newProps = this.props;
        if(oldProps.loading !== newProps.loading) {
          this.setState({labels: newProps.labelData})
        }
      }

    handleChange = (selectedOption) => {
        this.setState({
            value:  selectedOption,
            shortDescription: selectedOption.shortDescription,
            longDescription: selectedOption.longDescription
        });
      }

    render() {
        if (this.props.loading) {
            return (
              <div><p>loading.....</p></div>
            )
        } 
        else {
            if (this.props.labelData) {
                let labels = this.state.labels
                return (
                    <div className="searchBar">
                    <Select
                        className="labelSelector"
                        value={this.state.value}
                        onChange={this.handleChange}
                        options={labels}
                    />
                    <div className="description-box">
                        <p className="desc-sml">
                            {this.state.shortDescription}
                        </p>
                        {this.state.longDescription}
                    </div>
                    <Button className='labelBtn' onClick={() => {this.props.makeVote(this.state.value.label)}} outlined>LABEL PHRASE</Button>
                    </div>
                )
            }
        }
    }
}

export default connect((state) => ({ loading: state.label.loading, labelData: state.label.labelData }))(Searchbar);