import React, { Component } from 'react'
import { connect } from 'react-redux'
import { getNextPhrase } from './api'
import { voteForPhraseLabel } from './api'
import Searchbar from './searchbar'


import {
  Card,
  CardPrimaryAction,
  CardMedia,
  CardAction,
  CardActions,
  CardActionButtons,
  CardActionIcons
} from 'rmwc/Card';

import { Typography } from 'rmwc/Typography';

import { Grid, GridCell } from 'rmwc/Grid';
import { FormattedNumber, FormattedDate } from 'react-intl';

import { Fab } from 'rmwc/Fab';

import {
  Icon,
} from 'rmwc/Icon';






export class LabelPhrase extends Component {
  //state = { dirty: false }
  //handleChange = (val) => (evt) => { this.setState({ ...this.state, [val]: evt.target.value }) }


  componentDidMount() {
    this.props.dispatch(getNextPhrase());
  }


  makeVote(label) {
    //alert('voting ' + label + ' for ' + this.props.phraseData.id);
    this.props.dispatch(voteForPhraseLabel(this.props.phraseData.id, label));

    // HACk HACK need to move to react-thunk
    /*setTimeout(() => {
      this.props.dispatch(callGetNextPhrase());
    }, 1500)*/

  }


  render() {

    if (this.props.loading) {
      return (
        <div><p>loading.....</p></div>
      )
    } else {
      if (this.props.phraseData) {
        return (
          <div>
            <div style={{ position: 'fixed', bottom: '1rem', right: '1rem', zIndex: '1', textAlign: 'center' }}><Fab icon='delete' style={{ bottom: '0.5rem' }} onClick={() => {
              this.makeVote('TRASH');
            }}/><br /><Fab icon='skip_next' mini onClick={() => {
              this.makeVote('SKIPPED');
            }}/></div>
            <div><Typography style={{ width: '100%', textAlign: 'center' }} use="headline3" tag="h1">{this.props.phraseData.phrase}</Typography></div>
            <Grid>
              {this.props.phraseData.possibleLabels.map((label, i) => (
                <GridCell span="3" phone="4" tablet="2" desktop="3" key={i}>
                  <Card style={{ width: '100%' }}>
                    <CardPrimaryAction onClick={() => {
                      this.makeVote(label.label);
                    }}>
                      <div style={{ padding: '0 1rem 1rem 1rem' }}>
                        <Typography use="headline6" tag="h2">{label.label} - <FormattedNumber value={label.score * 100} minimumFractionDigits={2} />%</Typography>
                        <Typography
                          use="subtitle2"
                          tag="h3"
                          theme="text-secondary-on-background"
                          style={{ marginTop: '-1rem' }}
                        >
                          {label.shortDescription}
                        </Typography>
                        <Typography use="body1" tag="div" theme="text-secondary-on-background">{label.longDescription}</Typography>
                      </div>
                    </CardPrimaryAction>
                    <CardActions fullBleed>
                      <CardAction onClick={() => {
                        this.makeVote(label.label);
                      }}>Label phrase as {label.label} <Icon icon="arrow_forward" /></CardAction>
                    </CardActions>
                  </Card>
                </GridCell>

              ))}
            </Grid>

            < Searchbar makeVote={(label) => this.makeVote(label)}  />

          </div>
        )
      } else {
        return (
          <div><p>No more phrases</p></div>
        )
      }
    }
  }

}

export default connect((state) => ({ error: state.nextPhrase.error, loading: state.nextPhrase.loading, phraseData: state.nextPhrase.phraseData }))(LabelPhrase);


/*
 <GridTile key={i}>
                <GridTilePrimary>
                  <GridTilePrimaryContent>
                    <div>{label.longDescription}</div>
                  </GridTilePrimaryContent>
                </GridTilePrimary>
                <GridTileSecondary>
                  <GridTileIcon>info</GridTileIcon>
                  <GridTileTitle>{label.label} <br/> {label.shortDescription}</GridTileTitle>
                </GridTileSecondary>
              </GridTile>
              */


