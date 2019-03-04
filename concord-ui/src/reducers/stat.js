const userReducer = (state = { logged_in: false }, action) => {
    switch (action.type) {
        case 'CALL_GET_TOTAL_USER_VOTES':
            return { loading: true, error: false };
        case 'CALL_GET_TOTAL_USER_VOTES_FAILED':
            return { loading: false, error: true };
        case 'CALL_GET_TOTAL_USER_VOTES_SUCCEEDED':
            return { loading: false, error: false, userScores: action.userScores };
        default:
            return state
    }
};

export default userReducer