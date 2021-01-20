# Filter JSON

## Filter your JSON by key value!

This software provides a filter of an entity's data in a response to an API request.

By default, services may return some informations that your application doesn't need. In these cases, you'll need to use a projection to retrieve only necessary fields.

All the filtering process takes place recursively and it works for both JSON Object and JSON Array.

This software has been structured as indipendent microservice with a REST API but it can also be used as a library that can be integrated into your Java application.

To make possible the filtering process, application need all JSON Body in a POST request and field projections that are defined using the `?values=` on query parameter.

## Example with a JSON Object request

```
{
    "id" : int,      
    "foo": string,   
    "bar": boolean,
    "baz": Object
}
```

To retrieve id and foo fields, this POST call

`POST  https:/localhost:8080/api/v1/getJson?values=id,foo`

provides the following response:

```
{
    "foo": "string",
    "id": int
}
```

## Example with a JSON Array request

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

To retrieve id and foo values inside JSON Array, this POST call

`POST https:/localhost:8080/api/v1/getJson?values=id,list.foo`

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
