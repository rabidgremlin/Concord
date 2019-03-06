const errorReducer = (state = { hasError: false }, action) => {
  switch (action.type) {
    case "KILL_SESSION":
      return { hasError: false };
    case "RESET_ERROR":
      return { hasError: false };
    case "CALL_CREATE_SESSION":
      return { hasError: false };
    case "CALL_GET_NEXT_PHRASE":
      return { hasError: false };
    case "CALL_CREATE_SESSION_FAILED":
      return { hasError: true, msg: "Unable to login" };
    case "CALL_GET_NEXT_PHRASE_FAILED":
      return { hasError: true, msg: "Failed to retrieve next phrase" };
    case "CALL_VOTE_FOR_PHRASE_LABEL_FAILED":
      return { hasError: true, msg: "Failed save vote" };
    case "CALL_GET_ALL_LABELS":
      return { hasError: false };
    case "CALL_GET_ALL_LABELS_FAILED":
      return { hasError: true, msg: "Failed to get available labels" };
    default:
      return state;
  }
};

export default errorReducer;
