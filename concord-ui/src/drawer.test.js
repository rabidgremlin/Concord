import React from 'react';
import ReactDOM from 'react-dom';
import AppDrawer from './drawer';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16.2';


Enzyme.configure({ adapter: new Adapter() })

it('renders without crashing', () => {
  const wrapper = shallow(<AppDrawer/>);
  expect(wrapper.find(AppDrawer).children);
});
