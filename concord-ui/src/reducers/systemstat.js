const systemStatsReducer = (state = { loading: true, error: false }, action) => {
  switch (action.type) {
    case 'CALL_GET_SYSTEM_STATS':
      return { loading: true, error: false };
    case 'CALL_GET_SYSTEM_STATS_SUCCEEDED':
      return { loading: false, error: false, statsData: action.statsData };
    case 'CALL_GET_SYSTEM_STATS_FAILED':
      return { loading: false, error: true };
    default:
      return state;
  }
};

export default systemStatsReducer;
