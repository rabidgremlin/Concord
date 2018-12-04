import React from 'react';
import { Login } from './login';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16.2';
import { Grid  } from 'rmwc/Grid';
import { Card} from 'rmwc/Card';
import { Button } from 'rmwc/Button';

Enzyme.configure({ adapter: new Adapter() })


describe('login', () => {

    const login = shallow(<Login />);

    it('renders without crashing', () => {
        expect(login.find(Login));
        expect(login.find(Grid).length).toBe(1);
        expect(login.find(Grid).containsMatchingElement(Card));
        expect(login.find(Card).containsMatchingElement(Button));

    });

    it('contains the correct starting state', () => {
        expect(login.state('email')).toEqual("");
        expect(login.state('password')).toEqual("");
        expect(login.state('invalidData')).toEqual(true);
    });

});