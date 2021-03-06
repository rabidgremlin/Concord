import {
  callCreateSession,
  callCreateSessionFailed,
  callCreateSessionSucceeded,
  callDeleteVotesForPhrase,
  callDeleteVotesForPhraseFailed,
  callDeleteVotesForPhraseSucceeded,
  callGetAllLabels,
  callGetAllLabelsFailed,
  callGetAllLabelsSucceeded,
  callGetNextPhrase,
  callGetNextPhraseFailed,
  callGetNextPhraseSucceeded,
  callGetSystemStats,
  callGetSystemStatsFailed,
  callGetSystemStatsSucceeded,
  callGetUserStats,
  callGetUserStatsFailed,
  callGetUserStatsSucceeded,
  callPostPhrases,
  callPostPhrasesFailed,
  callPostPhrasesSucceeded,
  callResolveForPhraseLabel,
  callResolveForPhraseLabelFailed,
  callResolveForPhraseLabelSucceeded,
  callVoteForPhraseLabel,
  callVoteForPhraseLabelFailed,
  callVoteForPhraseLabelSucceeded,
  callDeleteLastVote,
  callDeleteLastVoteSucceeded,
  callDeleteLastVoteFailed
} from './actions';
import request from 'superagent';

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

export function resolveForPhraseLabel(phraseId, label) {
  return (dispatch) => {
    dispatch(callResolveForPhraseLabel());
    request
      .post('/api/phrases/' + phraseId + '/resolve')
      .send({ label: label })
      .set('Accept', 'application/json')
      .then(() => dispatch(callResolveForPhraseLabelSucceeded()))
      .catch((err) => dispatch(callResolveForPhraseLabelFailed(err)));
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
      .get('/api/stats/user')
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text))
      .then((data) => dispatch(callGetUserStatsSucceeded(data)))
      .catch((err) => dispatch(callGetUserStatsFailed(err)));
  };
}

export function postUnlabelledPhrases(unlabelledPhrases) {
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

export function getSystemStats() {
  return (dispatch) => {
    dispatch(callGetSystemStats());
    request
      .get('/api/stats/system')
      .set('Accept', 'application/json')
      .then((res) => JSON.parse(res.text))
      .then((data) => dispatch(callGetSystemStatsSucceeded(data)))
      .catch((err) => dispatch(callGetSystemStatsFailed(err)));
  };
}

export function deleteVotesForPhrase(phraseId) {
  return (dispatch) => {
    dispatch(callDeleteVotesForPhrase());
    request
      .delete('/api/phrases/' + phraseId + '/votes/delete')
      .set('Accept', 'application/json')
      .then(() => dispatch(callDeleteVotesForPhraseSucceeded()))
      .catch((err) => dispatch(callDeleteVotesForPhraseFailed(err)));
  };
}

export function deleteLastVote(phraseId) {
  return (dispatch) => {
    dispatch(callDeleteLastVote());
    request
      .delete('/api/phrases/' + phraseId + '/delete')
      .set('Accept', 'application/json')
      .then(() => dispatch(callDeleteLastVoteSucceeded()))
      .catch((err) => dispatch(callDeleteLastVoteFailed(err)));
  };
}