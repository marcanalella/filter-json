# Filter JSON

## Filter your JSON by key value!

This software provides a filter of an entity's data in a response to an API request.

By default, services may return some informations that your application doesn't need. In these cases, you'll need to use a projection to retrieve only necessary fields.

All the filtering process takes place recursively and it works for both JSON Object and JSON Array.

This software has been structured as indipendent microservice with a REST API

## How it works

Application expose `/api/v1/getJson` API to retrieve filtered JSON. Field projections have to be defined using the query param `?values=` with values followed by comma. 

If the data is nested, add a dot to the desired value to be filtered. 

Finally, to make possible the filtering process, application need all JSON Body in a POST request.

## Example with a JSON Object request

To retrieve id and foo fields, this POST call

`POST  https:/localhost:8080/api/v1/getJson?values=id,foo`

```
{
    "id" : int,      
    "foo": string,   
    "bar": boolean,
    "baz": Object
}
```

provides the following response:

```
{
    "id": int,
    "foo": "string"
}
```

## Example with a JSON Array request

To retrieve id and foo values inside JSON Array, this POST call

`POST https:/localhost:8080/api/v1/getJson?values=id,list.foo`
```
[
  {
      "id": 2,
      "three": {
          "point_1": "point_2",
          "point_3": 3.4
      },
      "list": [
          "foo" : 1,
          "two" : 2,
          "three" : 3 
      ]
  },
  {
      "id": 2,
      "three": {
          "point_1": "point_2",
          "point_3": 3.4
      },
      "list": [
          "foo" : 1,
          "two" : 2,
          "three" : 3 
      ]
  }
]
```

provides the following response:

```
[
  {
      "id": 2,
      "list": [
          "foo" : 1
      ]
  },
  {
      "id": 3,
      "list": [
          "foo" : 1
      ]
  }
]
```
