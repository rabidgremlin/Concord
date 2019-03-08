import { combineReducers } from 'redux';
import session from './session';
import nextPhrase from './phrase';
import error from './error';
import label from './label';
import userStats from './userstat';

export default combineReducers({
  session,
  nextPhrase,
  error,
  label,
  userStats
});
