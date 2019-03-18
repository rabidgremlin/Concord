import reducer from './uploadphrase';

describe('upload phrase reducer', () => {
  it('should return the initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle call to get user stats', () => {
    expect(
      reducer([], {
        type: 'CALL_POST_PHRASES'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle successful call to get user stats', () => {
    expect(
      reducer([], {
        type: 'CALL_POST_PHRASES_SUCCEEDED'
      })
    ).toEqual({
      loading: false,
      error: false
    });
  });

  it('should handle unsuccessful call to get user stats', () => {
    expect(
      reducer([], {
        type: 'CALL_POST_PHRASES_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });
});
