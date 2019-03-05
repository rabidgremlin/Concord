const labelReducer = (state = {loading: true, error: false}, action) => {
    switch (action.type) {
        case 'CALL_GET_ALL_LABELS':
            return {loading: true, error: false};
        case 'CALL_GET_ALL_LABELS_SUCCEEDED':
            return {loading: false, error: false, labelData: action.labelData};
        case 'CALL_GET_ALL_LABELS_FAILED':
            return {loading: false, error: true};
        default:
            return state
    }
};

export default labelReducer