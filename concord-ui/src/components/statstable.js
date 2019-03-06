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
        this.state = ({
            data: [],
            loading: true
        })
    }

    componentWillMount() {
        fetch('/api/stats')
            .then(results => results.json())
            // filter out users with less than 50 votes (they have inflated accuracy ratings)
            .then(results => results.filter((v, i) => results[i].totalVotes >= 50))
            .then(results => this.setState({data: results, loading: false}))
            .then(() => this.sortByTotal(-1));
    }

    toPercentage = (n, d) => (d > 0 ? 100 * (n / d) : 0).toFixed(2);

    render() {
        if (this.state.loading) {
            return (
                <div><p>loading...</p></div>
            )
        }
        const data = this.state.data;
        const dataLength = data.length;
        if (dataLength <= 0) {
            return (
                <div><p>No stats to display</p></div>
            )
        }
        return (
            <DataTable
                style={{minHeight: '500px', width: '100%'}}
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
                            <DataTableHeadCell alignEnd sort={this.state.totalWithConsensusSortDir || null}
                                               onSortChange={this.sortByTotalWithConsensus}>
                                Total Phrases <br/>(with consensus)
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd sort={this.state.completedSortDir || null}
                                               onSortChange={this.sortByCompleted}>
                                Completed Phrases
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd sort={this.state.trashSortDir || null}
                                               onSortChange={this.sortByTrashed}>
                                Trashed Phrases
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd sort={this.state.accuracyRateSortDir || null}
                                               onSortChange={this.sortByAccuracyRate}>
                                Accuracy Rating
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd sort={this.state.trashRateSortDir || null}
                                               onSortChange={this.sortByTrashedRate}>
                                Trash Rate
                            </DataTableHeadCell>
                            <DataTableHeadCell alignEnd sort={this.state.accuracyRateNoTrashSortDir || null}
                                               onSortChange={this.sortByAccuracyRateNoTrash}>
                                Accuracy Rating <br/>(without trashed phrases)
                            </DataTableHeadCell>
                        </DataTableRow>
                    </DataTableHead>
                    <DataTableBody>
                        {[...Array(dataLength)]
                            .map((v, i) => (
                                <DataTableRow key={i}>
                                    <DataTableCell>
                                        {data[i].userId}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].totalVotes.toLocaleString()}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].totalVotesWithConsensus.toLocaleString()}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].completedVotes.toLocaleString()}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {data[i].trashVotes.toLocaleString()}
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {this.toPercentage(data[i].completedVotes, data[i].totalVotesWithConsensus)}%
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {this.toPercentage(data[i].trashVotes, data[i].totalVotes)}%
                                    </DataTableCell>
                                    <DataTableCell alignEnd>
                                        {this.toPercentage(data[i].completedVotesIgnoringTrash, data[i].totalVotesWithConsensusIgnoringTrash)}%
                                    </DataTableCell>
                                </DataTableRow>
                            ))}
                    </DataTableBody>
                </DataTableContent>
            </DataTable>
        )
    }

    /**
     * Clears the column sort arrows
     */
    clearSorts() {
        this.setState({
            totalSortDir: null,
            totalWithConsensusSortDir: null,
            completedSortDir: null,
            trashSortDir: null,
            accuracyRateSortDir: null,
            trashRateSortDir: null,
            accuracyRateNoTrashSortDir: null,
        })
    }

    /**
     * Sort rows for the given property according to the supplier function
     */
    sortRows = (property, sortDir, supplier) => {
        this.clearSorts();
        this.setState({
            [property]: sortDir,
            data: this.state.data.sort((a, b) => sortDir * (supplier(a) - supplier(b)))
        })
    };

    sortByTotal = (sortDir) => this.sortRows('totalSortDir', sortDir, (a) => a.totalVotes);

    sortByTotalWithConsensus = (sortDir) => this.sortRows('totalWithConsensusSortDir', sortDir, (a) => a.totalVotesWithConsensus);

    sortByCompleted = (sortDir) => this.sortRows('completedSortDir', sortDir, (a) => (a.completedVotes));

    sortByTrashed = (sortDir) => this.sortRows('trashSortDir', sortDir, (a) => (a.trashVotes));

    sortByAccuracyRate = (sortDir) => this.sortRows('accuracyRateSortDir', sortDir, (a) => this.toPercentage(a.completedVotes, a.totalVotesWithConsensus));

    sortByTrashedRate = (sortDir) => this.sortRows('trashRateSortDir', sortDir, (a) => this.toPercentage(a.trashVotes, a.totalVotes));

    sortByAccuracyRateNoTrash = (sortDir) => this.sortRows('accuracyRateNoTrashSortDir', sortDir, (a) => this.toPercentage(a.completedVotesIgnoringTrash, a.totalVotesWithConsensusIgnoringTrash));

}
