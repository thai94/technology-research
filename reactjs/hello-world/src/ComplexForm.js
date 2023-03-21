import React from 'react';

class ComplexForm extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
           data: 'Initial data...'
        }

        this.updateState = this.updateState.bind(this);
     };

     updateState(e){
        this.setState({data: e.target.value})
     }

     render() {
         return (
             <div>
                 <ChildForm val={this.state.data} onUpdateForm={this.updateState}></ChildForm>
                 <p>{this.state.data}</p>
             </div>
         )
     }
}

class ChildForm extends React.Component {
    
    constructor(props) {
        super(props);
     };

     render() {
        return (
            <div>
                <input value={this.props.val} onChange={this.props.onUpdateForm}></input>
                <p>{this.props.val}</p>
            </div>
        )
     }
}

export default ComplexForm;