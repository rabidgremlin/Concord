import * as actions from './actions';

/**
 * HANLDE SESSION ####################
 */

describe('actions', () => {
  it('should create an action to create a session', () => {
    const expectedAction = {
      type: 'CALL_CREATE_SESSION'
    };
    expect(actions.callCreateSession()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action to reset an error flag', () => {
    const expectedAction = {
      type: 'RESET_ERROR'
    };
    expect(actions.resetError()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure to create a session', () => {
    const error = 'Failed to create';
    const expectedAction = {
      type: 'CALL_CREATE_SESSION_FAILED',
      error
    };
    expect(actions.callCreateSessionFailed(error)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for successful session creation', () => {
    const token = '1234';
    const expectedAction = {
      type: 'CALL_CREATE_SESSION_SUCCEEDED',
      token
    };
    expect(actions.callCreateSessionSucceeded(token)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action to kill session', () => {
    const expectedAction = {
      type: 'KILL_SESSION'
    };
    expect(actions.killSession()).toEqual(expectedAction);
  });
});

/**
 * HANDLE NEXT PHRASE ####################
 */

describe('actions', () => {
  it('should create an action to get next phrase', () => {
    const expectedAction = {
      type: 'CALL_GET_NEXT_PHRASE'
    };
    expect(actions.callGetNextPhrase()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure to get next phrase', () => {
    const error = 'Failed to retrieve phrase';
    const expectedAction = {
      type: 'CALL_GET_NEXT_PHRASE_FAILED',
      error
    };
    expect(actions.callGetNextPhraseFailed(error)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for successful next phrase retrieval', () => {
    const phraseData = {
      label: 'Hello',
      id: '1234'
    };
    const expectedAction = {
      type: 'CALL_GET_NEXT_PHRASE_SUCCEEDED',
      phraseData
    };
    expect(actions.callGetNextPhraseSucceeded(phraseData)).toEqual(expectedAction);
  });
});

/**
 * HANDLE VOTES ####################
 */

describe('actions', () => {
  it('should create an action to vote for a phrase label', () => {
    const expectedAction = {
      type: 'CALL_VOTE_FOR_PHRASE_LABEL'
    };
    expect(actions.callVoteForPhraseLabel()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure to get next phrase', () => {
    const error = 'Failed to retrieve phrase';
    const expectedAction = {
      type: 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED',
      error
    };
    expect(actions.callVoteForPhraseLabelFailed(error)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for successful next phrase retrieval', () => {
    const expectedAction = {
      type: 'CALL_VOTE_FOR_PHRASE_LABEL_SUCCEEDED'
    };
    expect(actions.callVoteForPhraseLabelSucceeded()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action to delete the last vote for a user', () => {
    const expectedAction = {
      type: 'CALL_DELETE_LAST_VOTE'
    };
    expect(actions.callDeleteLastVote()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for success on last vote delete', () => {
    const expectedAction = {
      type: 'CALL_DELETE_LAST_VOTE_SUCCEEDED'
    };
    expect(actions.callDeleteLastVoteSucceeded()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure on last vote delete', () => {
    const expectedAction = {
      type: 'CALL_DELETE_LAST_VOTE_FAILED'
    };
    expect(actions.callDeleteLastVoteFailed()).toEqual(expectedAction);
  });
});

/**
 *  HANDLE LABELS
 */

describe('actions', () => {
  it('should create an action to vote for a phrase label', () => {
    const expectedAction = {
      type: 'CALL_GET_ALL_LABELS'
    };
    expect(actions.callGetAllLabels()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure to get next phrase', () => {
    const error = 'Failed to retrieve phrase';
    const expectedAction = {
      type: 'CALL_GET_ALL_LABELS_FAILED',
      error
    };
    expect(actions.callGetAllLabelsFailed(error)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for successful next phrase retrieval', () => {
    const labelData = {
      NAME: 'Hello',
      id: '4321'
    };
    const expectedAction = {
      type: 'CALL_GET_ALL_LABELS_SUCCEEDED',
      labelData
    };
    expect(actions.callGetAllLabelsSucceeded(labelData)).toEqual(expectedAction);
  });
});

/**
 *  HANDLE POSTING PHRASES
 */

describe('actions', () => {
  it('should create an action to post list of phrases', () => {
    const expectedAction = {
      type: 'CALL_POST_PHRASES'
    };
    expect(actions.callPostPhrases()).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for failure to post list of phrases', () => {
    const error = 'error';
    const expectedAction = {
      type: 'CALL_POST_PHRASES_FAILED',
      error
    };
    expect(actions.callPostPhrasesFailed(error)).toEqual(expectedAction);
  });
});

describe('actions', () => {
  it('should create an action for successful posting of list of phrases', () => {
    const expectedAction = {
      type: 'CALL_POST_PHRASES_SUCCEEDED'
    };
    expect(actions.callPostPhrasesSucceeded()).toEqual(expectedAction);
  });
});
