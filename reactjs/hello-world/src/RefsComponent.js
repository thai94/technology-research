import React from 'react';
import ReactDOM from 'react-dom';

class RefsComponent extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            data: ''
         }
         this.updateVal = this.updateVal.bind(this)
         this.clearInput = this.clearInput.bind(this)
    }

    updateVal(e) {
        this.setState({
            data: e.target.value
        })
    }

    clearInput(e) {
        this.setState({
            data: ''
        })
        ReactDOM.findDOMNode(this.refs.myInput).focus();
    }

    render() {
        return (
            <div>
                <input value={this.state.data} ref="myInput" onChange={this.updateVal}></input>
                <button onClick={this.clearInput}>Clear</button>
            </div>
        )
    }
}

export default RefsComponent;