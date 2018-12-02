//https://redux.js.org/basics/example-todo-list


export const callCreateSession = () => ({
    type: 'CALL_CREATE_SESSION'
})

export const callCreateSessionFailed = (error) => ({
    type: 'CALL_CREATE_SESSION_FAILED',
    error
})

export const callCreateSessionSucceeded = (token) => ({
    type: 'CALL_CREATE_SESSION_SUCCEEDED',
    token
})

export const killSession = () => ({
    type: 'KILL_SESSION'
})


export const callGetNextPhrase = () => ({
    type: 'CALL_GET_NEXT_PHRASE'   
})

export const callGetNextPhraseFailed = (error)=> ({
    type: 'CALL_GET_NEXT_PHRASE_FAILED',
    error   
})

export const callGetNextPhraseSucceeded = (phraseData)=> ({
    type: 'CALL_GET_NEXT_PHRASE_SUCCEEDED',
    phraseData   
})


export const callVoteForPhraseLabel = () => ({
    type: 'CALL_VOTE_FOR_PHRASE_LABEL'
})

export const callVoteForPhraseLabelFailed = (error)=> ({
    type: 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED',
    error   
})

export const callVoteForPhraseLabelSucceeded = ()=> ({
    type: 'CALL_VOTE_FOR_PHRASE_LABEL_SUCCEEDED'
})

export const callGetAllLabels = () => ({
    type: 'CALL_GET_ALL_LABELS'
})

export const callGetAllLabelsFailed = (error) => ({
    type: 'CALL_GET_ALL_LABELS_FAILED',
    error
})

export const callGetAllLabelsSucceeded = (labelData) => ({
    type: 'CALL_GET_ALL_LABELS_SUCCEEDED',
    labelData
})