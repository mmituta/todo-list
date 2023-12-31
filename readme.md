# TODO Items Service

This project implements a service that allows for basic management of TODO list

## Running the service

To start the service you can download and run the docker image, or you can build the service yourself and run it
locally.

### Running the service using Docker

You can pull the docker image from the repository with the following command:
`docker pull mmituta/todo-list:v1`

and then to run it, you can use:
`docker run --name <<container-name>> -p <<port-number>>:8080 -d mmituta/todo-list:v1 `
where:

+ `<<container-name>>` is the name with which you want to create container from the image
+ `<<port-number>>` is the port on which the service should listen.

An example that starts the container named "todo" that listens on the port 8080:

`docker run --name todo -p 8080:8080 -d mmituta/todo-list:v1`

## Building and running the service locally

### Building the project

The project is managed by maven. It uses Java 21. To build it you can run the `mvn install` command.

#### Running the tests

The project provides a set of unit tests that can be run using `mvn test` command and integration tests that can be run
using `mvn failsafe:integration-test`

### Starting the service

After building the service locally, you can use the `mvn spring-boot:run` command to start it. The service is configured
to listen on the `8080` port by default.

### Building the docker image

To build and deploy the docker image use the `mvn compile jib:build -Dimage=<<TARGET_IMAGE>>` where `<<TARGET_IMAGE>>`
is the reference for the target image.

For example:
`mvn compile jib:build -Dimage=mmituta/todo-list:v1`

## Using the service

The service provides a page that documents the API, it contains a description of all the endpoints and their responses.
It also provides an environment to try out the service, available under:`<<base-url>>/swagger-ui/index.html`

So assuming that the service is run on localhost with the default `8080` port:

http://localhost:8080/swagger-ui/index.html

### Examples

This sections presents how some common use cases could be realized using the service's endpoints.

<details>
<summary><b>Add a new item</b></summary>

##### Endpoint:

`Method: POST; PATH: <<base-url>>/items` Example: http://localhost:8080/items

##### Request body:

```
{
    "description" : "second item",
    "dueDateTime" :  "2024-10-28T15:35:30Z"
}
```

#### Response body:

```
{
    "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
    "description": "second item",
    "dueDateTime": "2023-10-28T15:35:30Z",
    "created": "2023-10-26T18:03:28.4134419+02:00",
    "status": "NOT_DONE"
}
```
</details>

<details>
<summary><b>Change the description of an item</b></summary>

##### Endpoint

`Method: PATCH; PATH: <<base-url>>/items/{id}` Example: http://localhost:8080/items/492e9ca0-1dc3-4b6a-a64a-324657570d10

##### Request body:

```
{
    "description": "important item"
}
```

##### Response body:

```
{
    "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
    "description": "important item",
    "dueDateTime": "2023-10-28T15:35:30Z",
    "created": "2023-10-26T18:03:28.413442+02:00",
    "status": "NOT_DONE"
}
```
</details>

<details>
<summary><b>Mark an item as "done"</b></summary>

##### Endpoint

`Method: PATCH; PATH: <<base-url>>/items/{id}` Example: http://localhost:8080/items/492e9ca0-1dc3-4b6a-a64a-324657570d10

##### Request body:

```
{
    "status": "DONE"
}
```

##### Response body:

```
{
    "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
    "description": "important item",
    "dueDateTime": "2023-10-28T15:35:30Z",
    "created": "2023-10-26T18:03:28.413442+02:00",
    "finished": "2023-10-26T18:06:06.2633207+02:00",
    "status": "DONE"
}
```

</details>

<details>
<summary><b>Mark an item as "not done"</b></summary>

##### Endpoint:

`Method: PATCH; PATH: <<base-url>>/items/{id}` Example: http://localhost:8080/items/492e9ca0-1dc3-4b6a-a64a-324657570d10

##### Request body:

```
{
    "status": "NOT_DONE"
}
```

##### Response body

```
{
    "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
    "description": "important item",
    "dueDateTime": "2023-10-28T15:35:30Z",
    "created": "2023-10-26T18:03:28.413442+02:00",
    "status": "NOT_DONE"
}
```

</details>

<details>
<summary><b>Get all items that are "not done"</b></summary>

##### Endpoint

`Method: GET; PATH: <<base-url>>/items?status=?` Example: http://localhost:8080/items?status=NOT_DONE

##### Response body:

```
[
    {
        "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
        "description": "important item",
        "dueDateTime": "2023-10-28T15:35:30Z",
        "created": "2023-10-26T18:03:28.413442+02:00",
        "status": "NOT_DONE"
    },
    {
        "id": "ed5ca7cc-3d92-43a2-8686-de9c76211104",
        "description": "first item",
        "dueDateTime": "2024-01-28T15:35:30Z",
        "created": "2023-10-26T18:08:11.994788+02:00",
        "status": "NOT_DONE"
    }
]
```
</details>

<details>
<summary><b>Get all items</b></summary>

##### Endpoint

`Method: GET; PATH: <<base-url>>/items` Example: http://localhost:8080/items

##### Response body:

```
[
	{
		"id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
		"description": "important item",
		"dueDateTime": "2023-10-28T15:35:30Z",
		"created": "2023-10-26T18:03:28.413442+02:00",
		"status": "NOT_DONE"
	},
	{
		"id": "45dbb6b8-0da7-489c-882a-5f65898d977b",
		"description": "first item",
		"dueDateTime": "2023-01-28T15:35:30Z",
		"created": "2023-10-26T18:08:07.486186+02:00",
		"status": "PAST_DUE"
	},
	{
		"id": "ed5ca7cc-3d92-43a2-8686-de9c76211104",
		"description": "first item",
		"dueDateTime": "2024-01-28T15:35:30Z",
		"created": "2023-10-26T18:08:11.994788+02:00",
		"status": "NOT_DONE"
	}
]
```
</details>
<details>
<summary><b>Get details of a specific item</b></summary>

##### Endpoint

`Method: GET; PATH: <<base-url>>/items` Example: http://localhost:8080/items/492e9ca0-1dc3-4b6a-a64a-324657570d10

##### Response Body:

```
{
    "id": "492e9ca0-1dc3-4b6a-a64a-324657570d10",
    "description": "important item",
    "dueDateTime": "2023-10-28T15:35:30Z",
    "created": "2023-10-26T18:03:28.413442+02:00",
    "status": "NOT_DONE"
}
```
</details>

## Made assumptions

The requirement was that the service should automatically change the status of the items to "PAST_DUE" if the due date
time has passed.
My first instinct was to use a scheduler that would cyclically check all items and mark the ones that are past due.
In the end I had decided against it because:

+ To determine if the item is past due or not we can use the past due date property and compare it with the current
  time. Changing the status property of the item would be redundant.
+ Checking all the items would introduce a performance weak point. The time it would take to check the items would grow
  with the number of items
+ Scaling the service would be more complicated as the scheduler would need to run only on one node. It would require
  using a distributed lock or dividing the items between the nodes in some way.

It would make sense to introduce such scheduler if there would be a requirement to react on the event of the item being
past due. E.g. sending out events to other services, notifying users etc.

## Used libraries and tools

+ [REST-assured](https://rest-assured.io/) library has been used for writing e2e tests, it provides a concise interface
  with the BDD syntax
+ [Mapstruct](https://mapstruct.org/) has been used for mapping and also to avoid writing a lot of boilerplate for the
  PATCH partial item update
+ [Lombok](https://projectlombok.org/) has been used to avoid Java's boilerplate (setters, getters, equals etc)
+ [Jib maven plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin) has been used to
  dockerize the service, as the tools provided by Spring Boot couldn't handle Java 21
+ [OpenAPI](https://www.openapis.org/) has been used to document the endpoints and provide a page where the endpoints
  could be tested using Swagger

## Points to improve

Here's the list of things missing from the service that should be in place in a production-ready code.

+ I would add logging. A request filter could be enabled that allows for logging requests. There's not much to log in
  the application itself, maybe I'd add a DEBUG or TRACE entries to indicate that the item was mark DONE/NOT_DONE or
  that the update was prevented because of an item being past due.
+ Some kind of healthcheck endpoint. To allow checking the status of the service.
+ Better validation of the input data. For example, at the moment it's possible to create an item with past due date in
  the past. It's left out to make testing the behavior of the service easier but in the production code it would not
  make sense.
+ Docker set up could be improved. The image could be optimized to be lighter.
+ Error message returned by the endpoints should be improved. An Error type could be defined with an error code.
+ Validation messages should be added. Right now when the user makes a bad request it will just result in a 400 status
  with no message to specify what's causing the problem
+ Different DTOs could be introduced for the 'get all' and 'get details' endpoints. Get all could return only the basic
  information that the user could use to ask for more details.
+ The 'get all' endpoint should support paging
+ A service level model, separate from the JPA model could be introduced in the future. Currently, the entity is being
  used on the data access and on the service layer, which is fine for such simple case. But in more complex application
  it can lead to the service design being affected by the limitation's of the entities.
+ Method-level precondition checks should be added. For example right now there's a validation on the controller level
  that doesn't accept blank strings when updating the item's description. I use this fact on the service level, without
  implicitly checking if the value is blank. If the service would be used in another context this could lead to
  inconsistent behavior
+ I'd consider adding more JavaDocs. Right now only the 'interesting' parts of the application are documented. The rest
  of the classes and methods is relatively simple and can be understood without the comments, so I didn't add them.