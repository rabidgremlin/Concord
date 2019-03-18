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
import { UploadPhrase } from './uploadphrase';
import { Dialog, DialogActions, DialogButton, DialogContent, DialogTitle } from '@rmwc/dialog';
import { Button } from '@rmwc/button';
import { TextField } from '@rmwc/textfield';

Enzyme.configure({ adapter: new Adapter() });

function setup() {
  const props = {
    dispatch: jest.fn()
  };
  const uploadPhrase = shallow(<UploadPhrase {...props} />);
  console.log(uploadPhrase.debug());
  return {
    props,
    uploadPhrase
  };
}

describe('UploadPhrase', () => {
  it('renders without crashing', () => {
    const { uploadPhrase, props } = setup();
    expect(uploadPhrase.find(UploadPhrase));

    expect(uploadPhrase.find(UploadPhrase).containsMatchingElement(TextField));

    expect(uploadPhrase.find(UploadPhrase).containsMatchingElement(Button));

    expect(uploadPhrase.find(UploadPhrase).containsMatchingElement(DataTable));
    expect(uploadPhrase.find(DataTable).containsMatchingElement(DataTableContent));
    expect(uploadPhrase.find(DataTableContent).containsMatchingElement(DataTableHead));
    expect(uploadPhrase.find(DataTableContent).containsMatchingElement(DataTableBody));
    expect(uploadPhrase.find(DataTableBody).containsMatchingElement(DataTableRow));
    expect(uploadPhrase.find(DataTableRow).containsMatchingElement(DataTableCell));
    expect(uploadPhrase.find(DataTableRow).containsMatchingElement(DataTableHeadCell));

    expect(uploadPhrase.find(UploadPhrase).containsMatchingElement(Dialog));
    expect(uploadPhrase.find(Dialog).containsMatchingElement(DialogTitle));
    expect(uploadPhrase.find(Dialog).containsMatchingElement(DialogContent));
    expect(uploadPhrase.find(Dialog).containsMatchingElement(DialogActions));
    expect(uploadPhrase.find(DialogActions).containsMatchingElement(DialogButton));
  });
});
