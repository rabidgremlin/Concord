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
import { SystemStatsTable } from './systemstatstable';

Enzyme.configure({ adapter: new Adapter() });

function setup() {
  const props = {
    enableRefresh: () => {},
    dispatch: jest.fn()
  };
  const statsTable = shallow(<SystemStatsTable {...props} />);
  console.log(statsTable.debug());
  return {
    props,
    statsTable
  };
}

describe('SystemStatsTable', () => {
  it('renders without crashing', () => {
    const { statsTable, props } = setup();
    expect(statsTable.find(SystemStatsTable));
    expect(statsTable.find(SystemStatsTable).containsMatchingElement(DataTable));
    expect(statsTable.find(DataTable).containsMatchingElement(DataTableContent));
    expect(statsTable.find(DataTableContent).containsMatchingElement(DataTableHead));
    expect(statsTable.find(DataTableHead).containsMatchingElement(DataTableRow));
    expect(statsTable.find(DataTableRow).containsMatchingElement(DataTableHeadCell));
    expect(statsTable.find(DataTableContent).containsMatchingElement(DataTableBody));
    expect(statsTable.find(DataTableBody).containsMatchingElement(DataTableRow));
    expect(statsTable.find(DataTableRow).containsMatchingElement(DataTableCell));
  });
});
