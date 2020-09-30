import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

class FormJSON extends Component {

  emptyItem = {
    json: '',
    values: '',
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

   async componentDidMount() {
     if (this.props.match.params.id !== 'new') {
       const group = await (await fetch(`/api/v1/getJson`)).json();
       this.setState({item: group});
     }
   }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {item} = this.state;

    await fetch('/api/v1/getJson?values=' + item.values, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item.json),
    });
  }

  render() {
    const {item} = this.state;
    const title2 = <h2> </h2>;
    const title = <h2>Filter your JSON!</h2>;

    return <div>
      <AppNavbar/>
      <Container>
      {title2}
        {title}
        <Form onSubmit={this.handleSubmit}>
          <FormGroup>
            <Label for="name">Your JSON</Label>
            <Input type="text" name="json" id="json" value={item.json || ''}
                   onChange={this.handleChange} autoComplete="JSON"/>
          </FormGroup>
          <FormGroup>
            <Label for="address">Values</Label>
            <Input type="text" name="values" id="values" value={item.values || ''}
                   onChange={this.handleChange} autoComplete="values"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Submit</Button>{' '}
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(FormJSON);