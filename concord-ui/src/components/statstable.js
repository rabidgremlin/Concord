import React, {Component} from 'react';

export default class StatsTable extends Component {

    render() {
        console.log("Rendering stats");
        fetch('/api/stats')
            .then(results => results.json())
            .then(data => {
                data.forEach(console.log);
            });
        return (
            <div>STATS</div>
        )
    }

}
