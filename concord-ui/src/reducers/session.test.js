import reducer from './session'

describe('session reducer', () => {

    it('should return the initial state', () => {
        expect(reducer(undefined, {})).toEqual(
            {
                logged_in: false
            }
        )
    });

    it('should handle sccessful call to create session', () => {
        const testToken = '123ABC';
        expect(
            reducer([], {
                type: 'CALL_CREATE_SESSION_SUCCEEDED',
                token: testToken
            })
        ).toEqual(
            {
                logged_in: true,
                token: testToken
            }
        )
    });

    it('should handle failure to create session', () => {
        expect(
            reducer([], {
                type: 'CALL_CREATE_SESSION_FAILED'
            })
        ).toEqual(
            {
                logged_in: false
            }
        )
    });

    it('should handle call to kill session', () => {
        expect(
            reducer([], {
                type: 'KILL_SESSION'
            })
        ).toEqual(
            {
                logged_in: false
            }
        )
    });

});