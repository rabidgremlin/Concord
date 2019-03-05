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
        if (data) {
            console.log("Rendering stats");
            return (
                <DataTable
                    style={{height: window.innerHeight, width: window.innerWidth}}
                >
                    <DataTableContent style={{fontSize: '20px'}}>
                        <DataTableHead>
                            <DataTableRow>
                                <DataTableHeadCell>
                                    User
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd sort={this.state.totalSortDir || null}
                                                   onSortChange={this.sortByTotal}>
                                    Total Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd sort={this.state.completedSortDir || null}
                                                   onSortChange={this.sortByCompleted}>
                                    Completed Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd sort={this.state.trashSortDir || null}
                                                   onSortChange={this.sortByTrashed}>
                                    Trashed Phrases
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd sort={this.state.completedRateSortDir || null}
                                                   onSortChange={this.sortByCompletedRate}>
                                    Success Rate
                                </DataTableHeadCell>
                                <DataTableHeadCell alignEnd sort={this.state.trashRateSortDir || null}
                                                   onSortChange={this.sortByTrashedRate}>
                                    Trash Rate
                                </DataTableHeadCell>
                            </DataTableRow>
                        </DataTableHead>
                        <DataTableBody>
                            {[...Array(data.length)].map((v, i) => (
                                <DataTableRow key={i}>
                                    <DataTableCell>{data[i].userId}</DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].totalVotes}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].completedVotes}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].trashVotes}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {(100 * data[i].completedSuccessRatio).toFixed(2)}%
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {(100 * data[i].trashRatio).toFixed(2)}%
                                    </DataTableCell>
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

    /**
     * Clears the column sort arrows
     */
    clearSorts() {
        this.setState({
            totalSortDir: null,
            completedSortDir: null,
            trashSortDir: null,
            completedRateSortDir: null,
            trashRateSortDir: null,
        })
    }

    /**
     * Sort rows and update the data state, render() will then update the UI
     */
    sortRows = (sortDir, comparatorFunction) => {
        const data = this.state.data;
        data.sort(comparatorFunction);
        this.setState({data: data})
    };

    sortByTotal = (sortDir) => {
        console.log("Sorting by total");
        this.clearSorts();
        this.setState({totalSortDir: sortDir});
        this.sortRows(sortDir, (a, b) => sortDir * (a.totalVotes - b.totalVotes));
    };

    sortByCompleted = (sortDir) => {
        console.log("Sorting by completed");
        this.clearSorts();
        this.setState({completedSortDir: sortDir});
        this.sortRows(sortDir, (a, b) => sortDir * (a.completedVotes - b.completedVotes));
    };

    sortByTrashed = (sortDir) => {
        console.log("Sorting by trashed");
        this.clearSorts();
        this.setState({trashSortDir: sortDir});
        this.sortRows(sortDir, (a, b) => sortDir * (a.trashVotes - b.trashVotes));
    };

    sortByCompletedRate = (sortDir) => {
        console.log("Sorting by completed rate");
        this.clearSorts();
        this.setState({completedRateSortDir: sortDir});
        this.sortRows(sortDir, (a, b) => sortDir * (a.completedSuccessRatio - b.completedSuccessRatio));
    };

    sortByTrashedRate = (sortDir) => {
        console.log("Sorting by trashed rate");
        this.clearSorts();
        this.setState({trashRateSortDir: sortDir});
        this.sortRows(sortDir, (a, b) => sortDir * (a.trashRatio - b.trashRatio));
    };

}
