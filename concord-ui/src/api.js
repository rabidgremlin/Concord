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
  callGetUserStatsSucceeded
} from './actions';

export function createSession(userId, password) {
  return (dispatch) => {
    dispatch(callCreateSession());
    request
      .post('/api/sessions')
      .send({ userId: userId, password: password })
      .set('Accept', 'application/json')
      .then((res) => {
        //console.log("res", JSON.stringify(res));
        const data = JSON.parse(res.text);
        //TODO: dispatch(itemsIsLoading(false));
        return data.token;
      })
      .then((token) => dispatch(callCreateSessionSucceeded(token)))
      .catch((err) => {
        //dispatch(itemsIsLoading(false));
        dispatch(callCreateSessionFailed(err));
      });
  };
}

export function getNextPhrase() {
  return (dispatch) => {
    dispatch(callGetNextPhrase());
    request
      .get('/api/phrases/next')
      //.send({ userId: userId, password: password })
      .set('Accept', 'application/json')
      .then((res) => {
        //console.log("res", JSON.stringify(res));
        //TODO: dispatch(itemsIsLoading(false));
        return JSON.parse(res.text);
      })
      .then((data) => dispatch(callGetNextPhraseSucceeded(data)))
      .catch((err) => {
        //dispatch(itemsIsLoading(false));
        dispatch(callGetNextPhraseFailed(err));
      });
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
