import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

import 'material-components-web/dist/material-components-web.css'
import WebFont from 'webfontloader'

WebFont.load({
  google: {
    families: ['Roboto:300,500,700','Material Icons']
  }
});

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
