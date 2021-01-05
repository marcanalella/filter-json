# Filter JSON

## Filter your JSON by key value!

This software provides a filter of an entity's data in a response to an API request.

By default, services may return some informations that your application doesn't need. In these cases, you'll need to use a projection to retrieve only necessary fields.

All the filtering process takes place recursively and it works for both JSON Object and JSON Array.

Field projections are defined using the `&values=` query parameter and narrowed by providing a comma-separated list of field names that you want returned as the value of the parameter.

This software can be used used as a microservice with a REST API or as a library that can be integrated into your Java application.

// TODO dockerize this Java application

## Sample JSON Object Example

```
{
    "id" : int,      
    "foo": string,   
    "bar": boolean,
    "baz": Object
}
```

To retrieve id and foo fields, this POST call to retrieve these objects 

`POST  https:/localhost:8080/api/v1/getJson&values=id,foo`

provides the following response:

```
{
    "foo": "string",
    "id": int
}
```

## Sample JSON Array Example

```
[
  {
      "one": 2,
      "three": {
          "point_1": "point_2",
          "point_3": 3.4
      },
      "list": [
          "one" : 1,
          "two" : 2,
          "three" : 3 
      ]
  },
  {
      "one": 2,
      "three": {
          "point_1": "point_2",
          "point_3": 3.4
      },
      "list": [
          "one" : 1,
          "two" : 2,
          "three" : 3 
      ]
  }
]
```

To retrieve one field and all one inside a JSON Array, this POST call to retrieve these objects 

`POST https:/localhost:8080/api/v1/getJson&values=list.one,one`

provides the following response:

```
[
  {
      "one": 2,
      "list": [
          "one" : 1
      ]
  },
  {
      "one": 3,
      "list": [
          "one" : 1
      ]
  }
]
```
