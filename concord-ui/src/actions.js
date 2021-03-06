/**
 * HANDLE SESSION
 */

export const callCreateSession = () => ({
  type: 'CALL_CREATE_SESSION'
});

export const resetError = () => ({
  type: 'RESET_ERROR'
});

export const callCreateSessionFailed = (error) => ({
  type: 'CALL_CREATE_SESSION_FAILED',
  error
});

export const callCreateSessionSucceeded = (token) => ({
  type: 'CALL_CREATE_SESSION_SUCCEEDED',
  token
});

export const killSession = () => ({
  type: 'KILL_SESSION'
});

/**
 * HANDLE NEXT PHRASE
 */

export const callGetNextPhrase = () => ({
  type: 'CALL_GET_NEXT_PHRASE'
});

export const callGetNextPhraseFailed = (error) => ({
  type: 'CALL_GET_NEXT_PHRASE_FAILED',
  error
});

export const callGetNextPhraseSucceeded = (phraseData) => ({
  type: 'CALL_GET_NEXT_PHRASE_SUCCEEDED',
  phraseData
});

/**
 * HANDLE VOTES
 */

export const callVoteForPhraseLabel = () => ({
  type: 'CALL_VOTE_FOR_PHRASE_LABEL'
});

export const callVoteForPhraseLabelFailed = (error) => ({
  type: 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED',
  error
});

export const callVoteForPhraseLabelSucceeded = () => ({
  type: 'CALL_VOTE_FOR_PHRASE_LABEL_SUCCEEDED'
});

/**
 * HANDLE RESOLVING PHRASES
 */

export const callResolveForPhraseLabel = () => ({
  type: 'CALL_RESOLVE_FOR_PHRASE_LABEL'
});

export const callResolveForPhraseLabelFailed = (error) => ({
  type: 'CALL_RESOLVE_FOR_PHRASE_LABEL_FAILED',
  error
});

export const callResolveForPhraseLabelSucceeded = () => ({
  type: 'CALL_RESOLVE_FOR_PHRASE_LABEL_SUCCEEDED'
});

/**
 * HANDLE LABELS
 */

export const callGetAllLabels = () => ({
  type: 'CALL_GET_ALL_LABELS'
});

export const callGetAllLabelsFailed = (error) => ({
  type: 'CALL_GET_ALL_LABELS_FAILED',
  error
});

export const callGetAllLabelsSucceeded = (labelData) => ({
  type: 'CALL_GET_ALL_LABELS_SUCCEEDED',
  labelData
});

/**
 * HANDLE STATS
 */

export const callGetUserStats = () => ({
  type: 'CALL_GET_USER_STATS'
});

export const callGetUserStatsFailed = (error) => ({
  type: 'CALL_GET_USER_STATS_FAILED',
  error
});

export const callGetUserStatsSucceeded = (data) => ({
  type: 'CALL_GET_USER_STATS_SUCCEEDED',
  statsData: data
});

export const callGetSystemStats = () => ({
  type: 'CALL_GET_SYSTEM_STATS'
});

export const callGetSystemStatsFailed = (error) => ({
  type: 'CALL_GET_SYSTEM_STATS_FAILED',
  error
});

export const callGetSystemStatsSucceeded = (data) => ({
  type: 'CALL_GET_SYSTEM_STATS_SUCCEEDED',
  statsData: data
});

/**
 * HANDLE UPLOADING PHRASES
 */

export const callPostPhrases = () => ({
  type: 'CALL_POST_PHRASES'
});

export const callPostPhrasesFailed = (error) => ({
  type: 'CALL_POST_PHRASES_FAILED',
  error
});

export const callPostPhrasesSucceeded = () => ({
  type: 'CALL_POST_PHRASES_SUCCEEDED'
});

/**
 *  HANDLE DELETING VOTES
 */
export const callDeleteVotesForPhrase = () => ({
  type: 'CALL_DELETE_VOTES_FOR_PHRASE'
});

export const callDeleteVotesForPhraseFailed = (error) => ({
  type: 'CALL_DELETE_VOTES_FOR_PHRASE_FAILED',
  error
});

export const callDeleteVotesForPhraseSucceeded = () => ({
  type: 'CALL_DELETE_VOTES_FOR_PHRASE_SUCCEEDED'
});

export const callDeleteLastVote = () => ({
  type: 'CALL_DELETE_LAST_VOTE'
});

export const callDeleteLastVoteFailed = (error) => ({
  type: 'CALL_DELETE_LAST_VOTE_FAILED',
  error
});

export const callDeleteLastVoteSucceeded = () => ({
  type: 'CALL_DELETE_LAST_VOTE_SUCCEEDED'
});

