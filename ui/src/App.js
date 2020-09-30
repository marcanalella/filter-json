import React, { Component } from 'react';
import './App.css';
import Form from './Form';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
        <Route path='/' exact={true} component={Form}/>
        </Switch>
      </Router>
    )
  }
}

export default App;