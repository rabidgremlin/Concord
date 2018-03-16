//https://redux.js.org/basics/example-todo-list


export const callCreateSession = (userId, password) => ({
    type: 'CALL_CREATE_SESSION',
    userId,
    password
})

export const callCreateSessionFailed = (error) => ({
    type: 'CALL_CREATE_SESSION_FAILED',
    error
})

export const callCreateSessionSucceeded = (token) => ({
    type: 'CALL_CREATE_SESSION_SUCCEEDED',
    token
})