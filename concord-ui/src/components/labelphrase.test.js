import React from "react";
import { LabelPhrase } from "./labelphrase";
import Enzyme, { shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import { Card, CardPrimaryAction, Fab, Grid, GridCell } from "rmwc";

Enzyme.configure({ adapter: new Adapter() });

function setup() {
  const props = {
    dispatch: jest.fn(),
    phraseData: {
      msg: "Hello world",
      id: 1234,
      possibleLabels: [
        {
          label: "HelloIntent",
          score: 0.5,
          shortDescription: "Hello intention",
          longDescription: "User saying hello"
        },
        {
          label: "GreetingIntent",
          score: 0.24,
          shortDescription: "Greeting intention",
          longDescription: "User saying a greeting"
        }
      ]
    }
  };
  const labelPhrase = shallow(<LabelPhrase {...props} />);
  // console.log(labelPhrase.debug());
  return {
    props,
    labelPhrase
  };
}

describe("labelPhrase", () => {
  const { labelPhrase, props } = setup();
  it("renders without crashing", () => {
    expect(labelPhrase.find(LabelPhrase));
    expect(labelPhrase.find(Grid).length).toBe(1);
    expect(labelPhrase.find(Grid).containsMatchingElement(Card));
    expect(labelPhrase.find(Card).containsMatchingElement(CardPrimaryAction));
    expect(labelPhrase.find(Grid).containsMatchingElement(GridCell));
    expect(labelPhrase.find(GridCell).length).toBe(3);
    expect(labelPhrase.find(Card).length).toBe(3);
    expect(labelPhrase.find(Fab).length).toBe(2);

    let delIcon = labelPhrase.find(Fab).get(0);
    let skipIcon = labelPhrase.find(Fab).get(1);

    expect(delIcon.props.icon === "delete").toEqual(true);
    expect(skipIcon.props.icon === "skip_next").toEqual(true);
  });
});
