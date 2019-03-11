import request from 'superagent';

import {
  callCreateSession,
  callCreateSessionFailed,
  callCreateSessionSucceeded,
  callGetAllLabels,
  callGetAllLabelsFailed,
  callGetAllLabelsSucceeded,
  callGetNextPhrase,
  callGetNextPhraseFailed,
  callGetNextPhraseSucceeded,
  callVoteForPhraseLabel,
  callVoteForPhraseLabelFailed,
  callVoteForPhraseLabelSucceeded,
  callGetUserStats,
  callGetUserStatsFailed,
  callGetUserStatsSucceeded,
  callPostPhrases,
  callPostPhrasesSucceeded,
  callPostPhrasesFailed
} from './actions';

export function createSession(userId, password) {
  return (dispatch) => {
    dispatch(callCreateSession());
    request
      .post('/api/sessions')
      .send({ userId: userId, password: password })
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text).token)
      .then((token) => dispatch(callCreateSessionSucceeded(token)))
      .catch((err) => dispatch(callCreateSessionFailed(err)));
  };
}

export function getNextPhrase() {
  return (dispatch) => {
    dispatch(callGetNextPhrase());
    request
      .get('/api/phrases/next')
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text))
      .then((data) => dispatch(callGetNextPhraseSucceeded(data)))
      .catch((err) => dispatch(callGetNextPhraseFailed(err)));
  };
}

export function voteForPhraseLabel(phraseId, label) {
  return (dispatch) => {
    dispatch(callVoteForPhraseLabel());
    request
      .post('/api/phrases/' + phraseId + '/votes/')
      .send({ label: label })
      .set('Accept', 'application/json')
      .then(() => dispatch(callVoteForPhraseLabelSucceeded()))
      .then(() => dispatch(getNextPhrase()))
      .catch((err) => dispatch(callVoteForPhraseLabelFailed(err)));
  };
}

export function getAllLabels() {
  return (dispatch) => {
    dispatch(callGetAllLabels());
    request
      .get('/api/labels')
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text))
      .then((data) => dispatch(callGetAllLabelsSucceeded(data)))
      .catch((err) => dispatch(callGetAllLabelsFailed(err)));
  };
}

export function getUserStats() {
  return (dispatch) => {
    dispatch(callGetUserStats());
    request
      .get('/api/stats')
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text))
      .then((data) => dispatch(callGetUserStatsSucceeded(data)))
      .catch((err) => dispatch(callGetUserStatsFailed(err)));
  };
}

export function postPhrases(unlabelledPhrases) {
  return (dispatch) => {
    dispatch(callPostPhrases());
    request
      .post('/api/phrases/bulk')
      .send({ unlabelledPhrases: unlabelledPhrases })
      .set('Accept', 'application/json')
      .then(() => dispatch(callPostPhrasesSucceeded()))
      .catch((err) => dispatch(callPostPhrasesFailed(err)));
  };
}
