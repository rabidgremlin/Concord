const postPhrasesReducer = (state = { loading: true, error: false }, action) => {
  switch (action.type) {
    case 'CALL_POST_PHRASES':
      return { loading: true, error: false };
    case 'CALL_POST_PHRASES_SUCCEEDED':
      return { loading: false, error: false };
    case 'CALL_POST_PHRASES_FAILED':
      return { loading: false, error: true };
    default:
      return state;
  }
};

export default postPhrasesReducer;
