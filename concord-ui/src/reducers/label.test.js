import reducer from './label';

describe('label reducer', () => {
  it('should return the initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle call to get all labels', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_ALL_LABELS'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle successful call to get all labels', () => {
    const testLabelData = {
      label: 'Hello',
      id: 123
    };
    expect(
      reducer([], {
        type: 'CALL_GET_ALL_LABELS_SUCCEEDED',
        labelData: testLabelData
      })
    ).toEqual({
      loading: false,
      error: false,
      labelData: testLabelData
    });
  });

  it('should handle unsuccessful call to get all labels', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_ALL_LABELS_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });
});
