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
} from '@rmwc/data-table';
import '@rmwc/data-table/data-table.css';
import { UserStatsTable } from './userstatstable';

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
  const statsTable = shallow(<UserStatsTable {...props} />);
  console.log(statsTable.debug());
  return {
    props,
    statsTable
  };
}

describe('UserStatsTable', () => {
  it('renders without crashing', () => {
    const { statsTable, props } = setup();
    expect(statsTable.find(UserStatsTable));
    expect(statsTable.find(UserStatsTable).containsMatchingElement(DataTable));
    expect(statsTable.find(DataTable).containsMatchingElement(DataTableContent));
    expect(statsTable.find(DataTableContent).containsMatchingElement(DataTableHead));
    expect(statsTable.find(DataTableHead).containsMatchingElement(DataTableRow));
    expect(statsTable.find(DataTableRow).containsMatchingElement(DataTableHeadCell));
    expect(statsTable.find(DataTableContent).containsMatchingElement(DataTableBody));
    expect(statsTable.find(DataTableBody).containsMatchingElement(DataTableRow));
    expect(statsTable.find(DataTableRow).containsMatchingElement(DataTableCell));
  });
});
