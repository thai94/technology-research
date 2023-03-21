import React from 'react';
import PropTypes from 'prop-types';

class Content extends React.Component {
    
    constructor(props) {
      super(props);
      this.state = {
        header: 'Header from state...',
        content: 'Content from state...'
      }
    }
    componentWillMount() {
      console.log('Component WILL MOUNT!')
   }
   componentDidMount() {
      console.log('Component DID MOUNT!')
   }
   componentWillReceiveProps(newProps) {    
      console.log('Component WILL RECIEVE PROPS!')
   }
   shouldComponentUpdate(newProps, newState) {
      return true;
   }
   componentWillUnmount() {
      console.log('Component WILL UNMOUNT!')
   }
    render() {
      return (
        <div>
          <h2>{this.state.header}</h2>
          <p>{this.state.content}</p>
          <p>{this.props.contentprop}</p>
        </div>
      )
    }
  }

const propTypes = {
    contentprop: PropTypes.number
}

Content.propTypes = propTypes

Content.defaultProps = {
    contentprop: 5000
}

export default Content;