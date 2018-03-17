import request from 'superagent'


import { callCreateSessionSucceeded } from './actions'
import { callCreateSessionFailed } from './actions'

export const apiService = store => next => action => {
    /*
    Pass all actions through by default
    */
    next(action)
    switch (action.type) {
        case 'CALL_CREATE_SESSION':
            /*
            In case we receive an action to send an API request, send the appropriate request
            */
            request
                .post('/api/sessions')
                .send({ userId: action.userId, password: action.password })               
                .set('Accept', 'application/json')
                .end((err, res) => {
                    if (err) {
                        /*
                        in case there is any error, dispatch an action containing the error
                        */
                        return next(callCreateSessionFailed(err))
                    }
                    const data = JSON.parse(res.text)
                    /*
                    Once data is received, dispatch an action telling the application
                    that data was received successfully, along with the parsed data
                    */
                    next(callCreateSessionSucceeded(data.token))
                })
            break
        /*
        Do nothing if the action does not interest us
        */
        default:
            break
    }

}

//export default apiService