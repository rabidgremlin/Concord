import React, {Component} from 'react';

import {
    DataTable,
    DataTableBody,
    DataTableCell,
    DataTableContent,
    DataTableHead,
    DataTableHeadCell,
    DataTableRow
} from 'rmwc/DataTable';
import '@rmwc/data-table/data-table.css';

export default class StatsTable extends Component {

    constructor(props) {
        super(props);
        this.state = ({})
    }


    componentWillMount() {
        console.log("Fetching stats");
        fetch('/api/stats')
            .then(results => results.json())
            .then(results => {
                this.setState({
                    data: results
                })
            });
    }

    render() {
        const data = this.state.data;
        console.log(data);
        if (data) {
            console.log("Rendering stats");
            data.forEach(console.log);
            console.log(data[0].userId);
            return (
                <DataTable
                    style={{height: window.innerHeight, width: window.innerWidth}}
                >
                    <DataTableContent>
                        <DataTableHead>
                            <DataTableRow>
                                <DataTableHeadCell>
                                    User
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd>
                                    Total Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd>
                                    Completed Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd>
                                    Trashed Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd>
                                    Completed Success Rate
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd>
                                    Trashed Rate
                                </DataTableHeadCell>
                            </DataTableRow>
                        </DataTableHead>
                        <DataTableBody>
                            {[...Array(data.length)].map((v, i) => (
                                <DataTableRow key={i}>
                                    <DataTableCell>{data[i].userId}</DataTableCell>
                                    <DataTableCell alignEnd>{data[i].totalVotes}</DataTableCell>
                                    <DataTableCell alignEnd>{data[i].completedVotes}</DataTableCell>
                                    <DataTableCell alignEnd>{data[i].trashVotes}</DataTableCell>
                                    <DataTableCell alignEnd>{(100 * data[i].completedSuccessRatio).toFixed(0)}%</DataTableCell>
                                    <DataTableCell alignEnd>{(100 * data[i].trashRatio).toFixed(0)}%</DataTableCell>
                                </DataTableRow>
                            ))}
                        </DataTableBody>
                    </DataTableContent>
                </DataTable>
            )
        } else {
            return (
                <div><p>Loading...</p></div>
            )
        }
    }

}
