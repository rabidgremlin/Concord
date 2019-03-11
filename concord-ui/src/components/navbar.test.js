import React from 'react';
import Navbar from './navbar';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import { Toolbar, ToolbarIcon, ToolbarRow, ToolbarSection, ToolbarTitle } from 'rmwc';

Enzyme.configure({ adapter: new Adapter() });

it('renders without crashing', () => {
  const wrapper = shallow(<Navbar />);
  expect(wrapper.find(Navbar));
  expect(wrapper.find(Navbar).containsMatchingElement(Toolbar));
  expect(wrapper.find(Toolbar).containsMatchingElement(ToolbarRow));
  expect(wrapper.find(ToolbarRow).containsMatchingElement(ToolbarSection));
  expect(wrapper.find(ToolbarSection).containsMatchingElement(ToolbarIcon));
  expect(wrapper.find(ToolbarSection).containsMatchingElement(ToolbarTitle));
});
