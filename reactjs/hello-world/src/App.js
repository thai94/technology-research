import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

import Content from './Content';
import DemoState from './DemoState';
import SimpleForm from './SimpleForm';
import ComplexForm from './ComplexForm';
import RefsComponent from './RefsComponent'
import Home from './Home'
import About from './About'
import ComponentCountA from './ComponentCountA'
import ComponentCountB from './ComponentCountB'

class App extends React.Component {
  constructor() {
    super();
    this.state = {
      data: [
        {
          "id": 1,
          "name": "Foo",
          "age": "20"
        },
        {
          "id": 2,
          "name": "Bar",
          "age": "30"
        },
        {
          "id": 3,
          "name": "Baz",
          "age": "40"
        }
      ],
      content: 'hello world'
    }
    this.updateContent = this.updateContent.bind(this);
  }
  updateContent() {
    this.setState({
      content: 'THai test'
    })
  }
  render() {
    return (
      <div>
        <Header />
        <Content contentprop={this.state.content} />
        <button onClick={this.updateContent}>Update content</button>
        <table>
          <tbody>
            {this.state.data.map((persion, i) => <TableRow key={i} data={persion}></TableRow>)}
          </tbody>
        </table>
        <DemoState></DemoState>
        <hr />
        <SimpleForm></SimpleForm>
        <ComplexForm></ComplexForm>
        <hr />
        <RefsComponent></RefsComponent>
        <hr />
        <Router>
        <div>
          <ul>
            <li><Link to="/home/100">Home</Link></li>
            <li><Link to="/about">About</Link></li>
          </ul>
        </div>
        <hr />
        {/* <Switch> */}
          <Route exact path="/home/:id">
            <Home />
          </Route>
          <Route path="/about">
            <About />
          </Route>
        {/* </Switch> */}
        </Router>
        <hr/>
        <ComponentCountA></ComponentCountA>
        <ComponentCountB></ComponentCountB>
      </div>
    )
  }
}

class Header extends React.Component {
  render() {
    return (
      <div>
        <h1>Header</h1>
      </div>
    )
  }
}



class TableRow extends React.Component {
  render() {
    return (
      <tr>
        <td>{this.props.data.id}</td>
        <td>{this.props.data.name}</td>
        <td>{this.props.data.age}</td>
      </tr>
    )
  }
}

export default App;
