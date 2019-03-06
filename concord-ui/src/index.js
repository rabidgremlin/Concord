import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
//import registerServiceWorker from './registerServiceWorker';
import { Provider } from 'react-redux';
import { applyMiddleware, compose, createStore } from 'redux';
import rootReducer from './reducers';
import { addLocaleData, IntlProvider } from 'react-intl';
import englishLocaleData from 'react-intl/locale-data/en';
import thunk from 'redux-thunk';
import 'material-components-web/dist/material-components-web.min.css';
import WebFont from 'webfontloader';

//import { apiService } from './api-service'

WebFont.load({
  google: {
    families: ['Roboto:300,500,700', 'Material Icons']
  }
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
  rootReducer,
  composeEnhancers(applyMiddleware(thunk))
);

//const store = createStore(rootReducer, composeEnhancers());

addLocaleData([...englishLocaleData]);

ReactDOM.render(
  <Provider store={store}>
    <IntlProvider locale='en'>
      <App />
    </IntlProvider>
  </Provider>,
  document.getElementById('root')
);

//registerServiceWorker();
