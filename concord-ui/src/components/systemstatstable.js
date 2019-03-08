import React, { Component } from 'react';
import { connect } from 'react-redux';
import { getSystemStats } from '../api';

export class SystemStatsTable extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {
    this.props.dispatch(getSystemStats());
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (oldProps.loading !== newProps.loading) {
      this.setState({ statsData: newProps.statsData });
    }
  }

  render() {
    if (this.props.loading || !this.props.statsData || !this.state.statsData) {
      return (
        <div>
          <p>loading...</p>
        </div>
      );
    }
    const data = this.state.statsData;
    console.log(data);
    return (
      <div>
        <p>system stats</p>
      </div>
    );
  }
}

export default connect((state) => ({
  error: state.systemStats.error,
  loading: state.systemStats.loading,
  statsData: state.systemStats.statsData
}))(SystemStatsTable);
