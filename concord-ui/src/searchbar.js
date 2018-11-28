import React, { Component } from 'react'
import { getAllLabels } from './api'
import { connect } from 'react-redux'
import { Select } from '@rmwc/select';
import { Button, ButtonIcon } from'@rmwc/button';




class Searchbar extends Component {

    componentDidMount() {
        console.log(this);
        this.props.dispatch(getAllLabels());
    }


    render() {
        
        return (
            <div>
              <Select
              style={{width: '300px'}}
                label="Other labels"
                outlined
                placeholder=""
                options={['Cookies', 'Pizza', 'Icecream']}
              />
              <Button outlined>LABEL PHRASE AS .....</Button>
            </div>
        )
    }
}

export default connect((state) => ({ loading: state.label.loading, labelData: state.label.labelData }))(Searchbar);


