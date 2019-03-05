import React, {Component} from 'react';

import {DataTable, DataTableContent, DataTableHead, DataTableRow, DataTableHeadCell, DataTableBody, DataTableCell} from 'rmwc/DataTable';
import '@rmwc/data-table/data-table.css';

export default class StatsTable extends Component {

    render() {
        console.log("Rendering stats");
        fetch('/api/stats')
            .then(results => results.json())
            .then(data => {
                data.forEach(console.log);
            });
        return (
            <DataTable
                style={{ height: '300px', width: '375px' }}
            >
                <DataTableContent>
                    <DataTableHead>
                        <DataTableRow>
                            <DataTableHeadCell>Items</DataTableHeadCell>
                            <DataTableHeadCell>
                                Quantity
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd>
                                Unit price
                            </DataTableHeadCell>
                        </DataTableRow>
                    </DataTableHead>
                    <DataTableBody>
                        <DataTableRow>
                            <DataTableCell>Cookies</DataTableCell>
                            <DataTableCell alignEnd>25</DataTableCell>
                            <DataTableCell alignEnd>$2.90</DataTableCell>
                        </DataTableRow>
                        <DataTableRow activated>
                            <DataTableCell>Pizza</DataTableCell>
                            <DataTableCell alignEnd>50</DataTableCell>
                            <DataTableCell alignEnd>$1.25</DataTableCell>
                        </DataTableRow>
                        <DataTableRow>
                            <DataTableCell>Icecream</DataTableCell>
                            <DataTableCell alignEnd>10</DataTableCell>
                            <DataTableCell alignEnd>$2.35</DataTableCell>
                        </DataTableRow>
                    </DataTableBody>
                </DataTableContent>
            </DataTable>
        )
    }

}
