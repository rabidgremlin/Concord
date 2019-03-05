import React from 'react';
import {App} from './App';
import Enzyme, {shallow} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

Enzyme.configure({adapter: new Adapter()});

it('renders without crashing', () => {
    const wrapper = shallow(<App/>);
    expect(wrapper.find(App));
});