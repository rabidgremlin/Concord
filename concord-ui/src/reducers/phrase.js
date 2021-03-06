const nextPhraseReducer = (state = { loading: true, error: false }, action) => {
  switch (action.type) {
    case 'CALL_GET_NEXT_PHRASE':
      return { loading: true, error: false };
    case 'CALL_GET_NEXT_PHRASE_SUCCEEDED':
      return { loading: false, error: false, phraseData: action.phraseData };
    case 'CALL_GET_NEXT_PHRASE_FAILED':
      return { loading: false, error: true };
    case 'CALL_VOTE_FOR_PHRASE_LABEL':
      return { loading: true, error: false };
    case 'CALL_VOTE_FOR_PHRASE_LABEL_SUCCEEDED':
      return { loading: false, error: false };
    case 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED':
      return { loading: false, error: true };
    case 'CALL_RESOLVE_FOR_PHRASE_LABEL':
      return { loading: true, error: false };
    case 'CALL_RESOLVE_FOR_PHRASE_LABEL_SUCCEEDED':
      return { loading: false, error: false };
    case 'CALL_RESOLVE_FOR_PHRASE_LABEL_FAILED':
      return { loading: false, error: true };
    case 'CALL_DELETE_VOTES_FOR_PHRASE':
      return { loading: true, error: false };
    case 'CALL_DELETE_VOTES_FOR_PHRASE_SUCCEEDED':
      return { loading: false, error: false };
    case 'CALL_DELETE_VOTES_FOR_PHRASE_FAILED':
      return { loading: false, error: true };
    case 'CALL_DELETE_LAST_VOTE':
      return { loading: true, error: false };
    case 'CALL_DELETE_LAST_VOTE_SUCCEEDED':
      return { loading: false, error: false };
    case 'CALL_DELETE_LAST_VOTE_FAILED':
      return { loading: false, error: true };
    default:
      return state;
  }
};

export default nextPhraseReducer;
