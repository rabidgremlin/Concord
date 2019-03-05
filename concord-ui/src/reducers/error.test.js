import reducer from './error'

describe('error reducer', () => {

    it('should handle kill session', () => {
        expect(
            reducer([], {
                type: 'KILL_SESSION'
            })
        ).toEqual(
            {
                hasError: false
            }
        )
    });

    it('should handle reset error', () => {
        expect(
            reducer([], {
                type: 'RESET_ERROR'
            })
        ).toEqual(
            {
                hasError: false
            }
        )
    });

    it('should handle create session', () => {
        expect(
            reducer([], {
                type: 'CALL_CREATE_SESSION'
            })
        ).toEqual(
            {
                hasError: false
            }
        )
    });

    it('should handle get next phrase', () => {
        expect(
            reducer([], {
                type: 'CALL_GET_NEXT_PHRASE'
            })
        ).toEqual(
            {
                hasError: false
            }
        )
    });


    it('should handle create session failure', () => {
        expect(
            reducer([], {
                type: 'CALL_CREATE_SESSION_FAILED'
            })
        ).toEqual(
            {
                hasError: true,
                msg: 'Unable to login'
            }
        )
    });

    it('should handle failure to get next phrase', () => {
        expect(
            reducer([], {
                type: 'CALL_GET_NEXT_PHRASE_FAILED'
            })
        ).toEqual(
            {
                hasError: true,
                msg: 'Failed to retrieve next phrase'
            }
        )
    });

    it('should handle failure to vote for phrase label', () => {
        expect(
            reducer([], {
                type: 'CALL_VOTE_FOR_PHRASE_LABEL_FAILED'
            })
        ).toEqual(
            {
                hasError: true,
                msg: 'Failed save vote'
            }
        )
    });

    it('should handle retrieving all labels', () => {
        expect(
            reducer([], {
                type: 'CALL_GET_ALL_LABELS'
            })
        ).toEqual(
            {
                hasError: false
            }
        )
    });

    it('should handle failure retrieving all labels', () => {
        expect(
            reducer([], {
                type: 'CALL_GET_ALL_LABELS_FAILED'
            })
        ).toEqual(
            {
                hasError: true,
                msg: 'Failed to get available labels'
            }
        )
    });

});