import { combineReducers } from 'redux';
import session from './session';
import nextPhrase from './phrase';
import error from './error';
import label from './label';
import stats from './stat';

export default combineReducers({
  session,
  nextPhrase,
  error,
  label,
  stats
});
