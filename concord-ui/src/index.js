import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import { Provider } from 'react-redux'
import { createStore , applyMiddleware, compose  } from 'redux'
import rootReducer from './reducers'


import { apiService } from './api-service'

import 'material-components-web/dist/material-components-web.min.css';
import WebFont from 'webfontloader'

WebFont.load({
  google: {
    families: ['Roboto:300,500,700', 'Material Icons']
  }
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(rootReducer, composeEnhancers(applyMiddleware(apiService))  )

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>
  , document.getElementById('root'));
registerServiceWorker();
