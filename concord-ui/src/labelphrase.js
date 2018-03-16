import React, {Component} from 'react'

class LabelPhrase extends Component {
  state = {phrase: ""}
  handleChange = (val) => (evt) => { this.setState( {...this.state, [val]: evt.target.value} ) }
  

  render(){
    return(
    <div><p>Phrase Labeler goes here</p></div>
    )
  }
}

export default LabelPhrase;