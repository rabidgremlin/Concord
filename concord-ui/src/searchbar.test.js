import React from 'react';
import ReactDOM from 'react-dom';
import { Searchbar } from './searchbar';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16.2';


Enzyme.configure({ adapter: new Adapter() })

function setup() {
    const props = {
        makeVote: jest.fn(),
        dispatch: jest.fn()
    }
    const enzymeWrapper = shallow(<Searchbar {...props} />)

    return {
        props,
        enzymeWrapper
    }
}


it('renders without crashing', () => {
  const wrapper = shallow(< Searchbar />);
  const { enzymeWrapper } = setup()
  expect(enzymeWrapper.find(Searchbar));
});
