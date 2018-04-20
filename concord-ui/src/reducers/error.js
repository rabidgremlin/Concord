const errorReducer = (state = { hasError: false }, action) => {
    switch (action.type) {
        case 'CALL_CREATE_SESSION':
            return { hasError: false }
        case 'CALL_GET_NEXT_PHRASE':
            return { hasError: false }
        case 'CALL_CREATE_SESSION_FAILED':
            return { hasError: true, msg: 'Unable to login' }
        case 'CALL_GET_NEXT_PHRASE_FAILED':
            return { hasError: true, msg: 'Failed to retrieve next phrase' }
        case 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED':
            return { hasError: true, msg: 'Failed save vote' }    
        default:
            return state
    }
}

export default errorReducer