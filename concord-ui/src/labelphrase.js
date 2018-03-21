import React, { Component } from 'react'
import { connect } from 'react-redux'
import { callGetNextPhrase } from './actions'

class LabelPhrase extends Component {
  //state = { phrase: "" }
  //handleChange = (val) => (evt) => { this.setState({ ...this.state, [val]: evt.target.value }) }

  componentDidMount() {
    this.props.dispatch(callGetNextPhrase())
  }


  render() {

    if (this.props.loading) {
      return (
        <div><p>loading.....</p></div>
      )
    } else {
      return (
        <div><p>{this.props.phraseData.id}</p></div>
      )
    }
  }
}

export default connect((state) => ({ error: state.nextPhrase.error, loading: state.nextPhrase.loading, phraseData: state.nextPhrase.phraseData }))(LabelPhrase);