const errorReducer = (state = { hasError: false }, action) => {
  switch (action.type) {
    case 'KILL_SESSION':
      return { hasError: false };
    case 'RESET_ERROR':
      return { hasError: false };

    case 'CALL_CREATE_SESSION':
      return { hasError: false };
    case 'CALL_CREATE_SESSION_FAILED':
      return { hasError: true, msg: 'Unable to login' };

    case 'CALL_GET_NEXT_PHRASE':
      return { hasError: false };
    case 'CALL_GET_NEXT_PHRASE_FAILED':
      return { hasError: true, msg: 'Failed to retrieve next phrase' };

    case 'CALL_VOTE_FOR_PHRASE_LABEL':
      return { hasError: false };
    case 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED':
      return { hasError: true, msg: 'Failed save vote' };

    case 'CALL_RESOLVE_FOR_PHRASE_LABEL':
      return { hasError: false };
    case 'CALL_RESOLVE_FOR_PHRASE_LABEL_FAILED':
      return { hasError: true, msg: 'Failed to resolve phrase' };

    case 'CALL_DELETE_VOTES_FOR_PHRASE':
      return { hasError: false };
    case 'CALL_DELETE_VOTES_FOR_PHRASE_FAILED':
      return { hasError: true, msg: 'Failed to delete votes for phrase' };
    case 'CALL_DELETE_LAST_VOTE':
      return { hasError: false};
    case 'CALL_DELETE_LAST_VOTE_FAILED':
      return { hasError: true, msg: 'Failed to delete last phrase vote' };    

    case 'CALL_GET_ALL_LABELS':
      return { hasError: false };
    case 'CALL_GET_ALL_LABELS_FAILED':
      return { hasError: true, msg: 'Failed to get available labels' };

    case 'CALL_GET_SYSTEM_STATS':
      return { hasError: false };
    case 'CALL_GET_SYSTEM_STATS_FAILED':
      return { hasError: true, msg: 'Failed to get system stats' };

    case 'CALL_GET_USER_STATS':
      return { hasError: false };
    case 'CALL_GET_USER_STATS_FAILED':
      return { hasError: true, msg: 'Failed to get user stats' };

    case 'CALL_POST_PHRASES':
      return { hasError: false };

    default:
      return state;
  }
};

export default errorReducer;
