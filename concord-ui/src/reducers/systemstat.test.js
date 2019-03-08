import reducer from './systemstat';

describe('stat reducer', () => {
  it('should return the initial state', () => {
    expect(reducer(undefined, {})).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle call to get system stats', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_SYSTEM_STATS'
      })
    ).toEqual({
      loading: true,
      error: false
    });
  });

  it('should handle successful call to get system stats', () => {
    const testLabelData = {
      phrases: 40000,
      votes: 70000
    };
    expect(
      reducer([], {
        type: 'CALL_GET_SYSTEM_STATS_SUCCEEDED',
        statsData: testLabelData
      })
    ).toEqual({
      loading: false,
      error: false,
      statsData: testLabelData
    });
  });

  it('should handle unsuccessful call to get system stats', () => {
    expect(
      reducer([], {
        type: 'CALL_GET_SYSTEM_STATS_FAILED'
      })
    ).toEqual({
      loading: false,
      error: true
    });
  });
});
