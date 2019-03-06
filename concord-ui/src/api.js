import request from "superagent";

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
  callVoteForPhraseLabelSucceeded
} from "./actions";

export function createSession(userId, password) {
  return dispatch => {
    dispatch(callCreateSession());
    request
      .post("/api/sessions")
      .send({ userId: userId, password: password })
      .set("Accept", "application/json")
      .then(res => {
        //console.log("res", JSON.stringify(res));
        const data = JSON.parse(res.text);
        //TODO: dispatch(itemsIsLoading(false));
        return data.token;
      })
      .then(token => dispatch(callCreateSessionSucceeded(token)))
      .catch(err => {
        //dispatch(itemsIsLoading(false));
        dispatch(callCreateSessionFailed(err));
      });
  };
}

export function getNextPhrase() {
  return dispatch => {
    dispatch(callGetNextPhrase());
    request
      .get("/api/phrases/next")
      //.send({ userId: userId, password: password })
      .set("Accept", "application/json")
      .then(res => {
        //console.log("res", JSON.stringify(res));
        const data = JSON.parse(res.text);
        //TODO: dispatch(itemsIsLoading(false));
        return data;
      })
      .then(data => dispatch(callGetNextPhraseSucceeded(data)))
      .catch(err => {
        //dispatch(itemsIsLoading(false));
        dispatch(callGetNextPhraseFailed(err));
      });
  };
}

export function voteForPhraseLabel(phraseId, label) {
  return dispatch => {
    dispatch(callVoteForPhraseLabel());
    request
      .post("/api/phrases/" + phraseId + "/votes/")
      .send({ label: label })
      .set("Accept", "application/json")
      .then(res => dispatch(callVoteForPhraseLabelSucceeded()))
      .then(() => dispatch(getNextPhrase()))
      .catch(err => {
        //dispatch(itemsIsLoading(false));
        dispatch(callVoteForPhraseLabelFailed(err));
      });
  };
}

export function getAllLabels() {
  return dispatch => {
    dispatch(callGetAllLabels());
    request
      .get("/api/labels")
      .set("Accept", "application/json")
      .then(res => {
        const data = JSON.parse(res.text);
        return data;
      })
      .then(data => dispatch(callGetAllLabelsSucceeded(data)))
      .catch(err => {
        dispatch(callGetAllLabelsFailed(err));
      });
  };
}
