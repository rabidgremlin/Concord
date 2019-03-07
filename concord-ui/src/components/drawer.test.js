import React from 'react';
import AppDrawer from './drawer';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import {
  Drawer,
  DrawerHeader,
  DrawerContent,
  ListItem,
  ListItemText
} from 'rmwc';
Enzyme.configure({ adapter: new Adapter() });
it('renders without crashing', () => {
  const wrapper = shallow(<AppDrawer />);
  expect(wrapper.find(AppDrawer));
  expect(wrapper.find(AppDrawer).containsMatchingElement(Drawer));
  expect(wrapper.find(Drawer).containsMatchingElement(DrawerHeader));
  expect(wrapper.find(Drawer).containsMatchingElement(DrawerContent));
});
