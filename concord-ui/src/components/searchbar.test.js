import React from "react";
import { Searchbar } from "./searchbar";
import Enzyme, { shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import { Select } from "@rmwc/select";
import { Button } from "@rmwc/button";

Enzyme.configure({ adapter: new Adapter() });

function setup() {
  const props = {
    dispatch: jest.fn()
  };
  const searchbar = shallow(<Searchbar {...props} />);
  console.log(searchbar.debug());
  return {
    props,
    searchbar
  };
}

describe("Searchbar", () => {
  it("renders without crashing", () => {
    const { searchbar, props } = setup();
    expect(searchbar.find(Searchbar));
    expect(searchbar.find(Searchbar).containsMatchingElement(Select));
    expect(searchbar.find(Searchbar).containsMatchingElement(Button));
  });
});
