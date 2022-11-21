import React from 'react';
import PropTypes from 'prop-types';

class DemoState extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
        this.setStateHandler = this.setStateHandler.bind(this);
        this.forceUpdateHandler = this.forceUpdateHandler.bind(this);
    }
    setStateHandler() {
        var item = "setState..."
        var myArray = this.state.data.slice();
        myArray.push(item);
        this.setState({data: myArray})
    }
    forceUpdateHandler() {
        this.forceUpdate()
    }
    componentWillUpdate(nextProps, nextState) {
        console.log('Component WILL UPDATE!');
     }
     componentDidUpdate(prevProps, prevState) {
        console.log('Component DID UPDATE!')
     }
    render() {
        return (
            <div>
                <button onClick={this.setStateHandler}>My Button</button>
                <h4>State array: {this.state.data}</h4>
                <h4>Random number:{Math.random()}</h4>
                <button onClick={this.forceUpdateHandler}>Force Update</button>
            </div>
        )
    }

}

export default DemoState;