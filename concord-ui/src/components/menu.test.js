import React from 'react';
import Menu from './menu';
import { Drawer, DrawerContent, DrawerHeader, DrawerTitle, Icon, List, MenuItem } from 'rmwc';
import { Link } from 'react-router-dom';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

Enzyme.configure({ adapter: new Adapter() });

it('renders without crashing', () => {
  const wrapper = shallow(<Menu />);
  expect(wrapper.find(Menu));
  expect(wrapper.find(Menu).containsMatchingElement(Drawer));
  expect(wrapper.find(Drawer).containsMatchingElement(DrawerHeader));
  expect(wrapper.find(DrawerHeader).containsMatchingElement(DrawerTitle));
  expect(wrapper.find(DrawerContent).containsMatchingElement(List));
  expect(wrapper.find(List).containsMatchingElement(Link));
  expect(wrapper.find(Link).containsMatchingElement(MenuItem));
  expect(wrapper.find(MenuItem).containsMatchingElement(Icon));
});
