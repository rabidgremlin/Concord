import React from 'react';
import { Login } from './login';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import { Button } from '@rmwc/button';
import { Card } from '@rmwc/card';
import { Grid } from '@rmwc/grid';

Enzyme.configure({ adapter: new Adapter() });

describe('login', () => {
  const login = shallow(<Login />);

  it('renders without crashing', () => {
    expect(login.find(Login));
    expect(login.find(Grid).length).toBe(1);
    expect(login.find(Grid).containsMatchingElement(Card));
    expect(login.find(Card).containsMatchingElement(Button));
  });

  it('contains the correct starting state', () => {
    expect(login.state('email')).toEqual('');
    expect(login.state('password')).toEqual('');
    expect(login.state('invalidData')).toEqual(true);
  });
});
