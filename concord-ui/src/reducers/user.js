const userReducer = (state = { logged_in: false }, action) => {
    switch (action.type) {
        case 'CALL_GET_USER_SCORES':
            return { loading: true, error: false };
        case 'CALL_GET_USER_SCORES_FAILED':
            return { loading: false, error: true };
        case 'CALL_GET_USER_SCORES_SUCCEEDED':
            return { loading: false, error: false, userScores: action.userScores };
        default:
            return state
    }
};

export default userReducer