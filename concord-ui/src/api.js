import request from 'superagent'


import { callCreateSessionSucceeded } from './actions'
import { callCreateSessionFailed } from './actions'

import { callGetNextPhraseSucceeded } from './actions'
import { callGetNextPhraseFailed } from './actions'

import { callVoteForPhraseLabelSucceeded } from './actions'
import { callVoteForPhraseLabelFailed } from './actions'

export function callCreateSession(userId, password) {
    return (dispatch) => {
        // TODO: dispatch(itemsIsLoading(true));
        request
            .post('/api/sessions')
            .send({ userId: userId, password: password })
            .set('Accept', 'application/json')
            .then((res) => {
                //console.log("res", JSON.stringify(res));
                const data = JSON.parse(res.text)
                //TODO: dispatch(itemsIsLoading(false));
                return data.token;
            })
            .then((token) => dispatch(callCreateSessionSucceeded(token)))
            .catch((err) => {
                //dispatch(itemsIsLoading(false));
                dispatch(callCreateSessionFailed(err));
            });
    }
};


export function callGetNextPhrase() {
    return (dispatch) => {
        // TODO: dispatch(itemsIsLoading(true));
        request
            .get('/api/phrases/next')
            //.send({ userId: userId, password: password })
            .set('Accept', 'application/json')
            .then((res) => {
                //console.log("res", JSON.stringify(res));
                const data = JSON.parse(res.text)
                //TODO: dispatch(itemsIsLoading(false));
                return data;
            })
            .then((data) => dispatch(callGetNextPhraseSucceeded(data)))
            .catch((err) => {
                //dispatch(itemsIsLoading(false));
                dispatch(callGetNextPhraseFailed(err));
            });
    }
};

export function callVoteForPhraseLabel(phraseId,label) {
    return (dispatch) => {
        // TODO: dispatch(itemsIsLoading(true));
        request
            .post('/api/phrases/' + phraseId + '/votes/')
            .send({ label: label })
            .set('Accept', 'application/json')
            .then((res) => dispatch(callVoteForPhraseLabelSucceeded()))
            .then(() => dispatch(callGetNextPhrase()))
            .catch((err) => {
                //dispatch(itemsIsLoading(false));
                dispatch(callVoteForPhraseLabelFailed(err));
            });
    }
};


