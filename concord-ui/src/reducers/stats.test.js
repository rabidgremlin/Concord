import reducer from './stat';

describe('stat reducer', () => {
  it('should return the initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle call to get user stats', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_USER_STATS'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle successful call to get user stats', () => {
    const testLabelData = {
      userId: 'Bob',
      totalVotes: 123
    };
    expect(
      reducer([], {
        type: 'CALL_GET_USER_STATS_SUCCEEDED',
        statsData: testLabelData
      })
    ).toEqual({
      loading: false,
      error: false,
      statsData: testLabelData
    });
  });

  it('should handle unsuccessful call to get user stats', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_USER_STATS_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });
});
