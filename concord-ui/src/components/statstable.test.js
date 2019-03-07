import React from 'react';
import Enzyme, { shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import {
  DataTable,
  DataTableBody,
  DataTableCell,
  DataTableContent,
  DataTableHead,
  DataTableHeadCell,
  DataTableRow
} from 'rmwc/DataTable';
import { StatsTable } from './statstable';

Enzyme.configure({ adapter: new Adapter() });

function setup() {
  const props = {
    dispatch: jest.fn(),
    statsData: [
      {
        userId: 'bob',
        totalVotes: 50,
        completedVotes: 10,
        trashVotes: 20,
        totalVotesWithConsensus: 10,
        completedVotesIgnoringTrash: 5,
        completedVotesWithConsensusIgnoringTrash: 5,
        totalVotesWithConsensusIgnoringTrash: 10
      }
    ]
  };
  const statsTable = shallow(<StatsTable {...props} />);
  console.log(statsTable.debug());
  return {
    props,
    statsTable
  };
}

describe('StatsTable', () => {
  it('renders without crashing', () => {
    const { statsTable, props } = setup();
    expect(statsTable.find(StatsTable));
    expect(statsTable.find(StatsTable).containsMatchingElement(DataTable));
    expect(
      statsTable.find(DataTable).containsMatchingElement(DataTableContent)
    );
    expect(
      statsTable.find(DataTableContent).containsMatchingElement(DataTableHead)
    );
    expect(
      statsTable.find(DataTableHead).containsMatchingElement(DataTableRow)
    );
    expect(
      statsTable.find(DataTableRow).containsMatchingElement(DataTableHeadCell)
    );
    expect(
      statsTable.find(DataTableContent).containsMatchingElement(DataTableBody)
    );
    expect(
      statsTable.find(DataTableBody).containsMatchingElement(DataTableRow)
    );
    expect(
      statsTable.find(DataTableRow).containsMatchingElement(DataTableCell)
    );
  });
});
