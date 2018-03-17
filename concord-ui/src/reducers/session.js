const sessionReducer = (state = { logged_in: false }, action) => {
    switch (action.type) {
        case 'CALL_CREATE_SESSION_SUCCEEDED':
            return { logged_in: true, token: action.token }
        case 'CALL_CREATE_SESSION_FAILED':
            return { logged_in: false }
        case 'KILL_SESSION':
            return { logged_in: false }
        default:
            return state
    }
}

export default sessionReducer