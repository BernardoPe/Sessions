# Phase 1

## Introduction

The first phase of the project consists of the development of an information system
to manage multiplayer game sessions.  
We implemented this first part of the project with `kotlin`,
`HTTP4K` library for the web server and the `kotlinx.serialization` library for JSON serialization.  
We covered the implemented features with unit tests using the `JUnit` library.  
The database is managed by `PostgreSQL` and the connections are made using the `JDBC` library.

## Modeling the database

### Conceptual model ###

The following diagram holds the Entity-Relationship model for the information managed by the system.

[ER Diagram](er_diagram.png)

We highlight the following aspects:

* (_include a list of relevant design issues_)

The conceptual model has the following restrictions:

* (_include a list of relevant design issues_)

### Physical Model ###


The physical model of the database is available [here](../../../../../sql/createTables.sql).

We highlight the following aspects of this model:

* (_include a list of relevant design issues_)

## Software organization

### Open-API Specification ###

[Open-api Specification](open-api.json)

In our Open-API specification, we highlight the following aspects:

We have 3 groups of endpoints: Games, Players, and Sessions.  

The types of request methods the api supports are :
    - GET
    - POST
    - PUT

The Games group has 3 endpoints: 
 - Game Creation : POST /games
 - Game Search : GET /games
 - Game Search by id : GET /games/{id}

The Players group has 2 endpoints: 
 - Player Creation : POST /players
 - Player Search by id : GET /players/{id}

The Sessions group has 4 endpoints: 
 - Session Creation : POST /sessions
 - Session Search : GET /sessions/{gid}/list
 - Session Search by id : GET /sessions/{id}
 - Add player to session : POST /sessions/{id}/players

Response codes are:
    - 200 OK
    - 201 Created
    - 400 Bad Request
    - 404 Not Found
    - 409 Conflict
    - 500 Internal Server Error

### Request Details


At the Server level, the request is routed to the appropriate handler based on the path and method of the request.

The handler then processes the client's request at the API level of the application, 
which is responsible for validating the request parameters and checking for any errors.

After checking for errors, the handler calls the appropriate service method to process the request at the service level.
The service layer is responsible for handling the business logic of the application. 

It checks if the business rules of the
specific service are being followed and then calls the appropriate data access method to interact with the database.

The database then sanitizes the input with prepared statements to prevent SQL injection attacks and returns the query result
to the service layer, which then returns the result to the handler, who is responsible for returning the HTTP response.

Internally in a request, the application uses the following most relevant classes:
- DTOs: Responsible for holding the data of the request and response. Can be serialized to JSON and deserialized from JSON. 
- Domain classes: Represent the entities of the application. Used for checking business rules and constrictions.
- Data Access classes: Responsible for interacting with the database.
- API, Service, and Data Access classes: Responsible for handling the request at the API, service, and data access levels.

And the following methods:
- `processRequest`: This method is responsible for processing the request at the API level. 
It is the main handler of the API layer and is responsible for processing the request and catching
any errors that may occur at the application level.
- `authHandler`: This method is responsible for checking if the request is authorized. It calls the `processRequest` method if the request is authorized.
- API methods: These methods are responsible for processing the request at the API level. They are called by the `processRequest` method in callback and are responsible for validating the request parameters and checking for any errors.
- Service methods: These methods are responsible for processing the request at the service level. They are called by the API methods and are responsible for handling the business logic of the application.
- Data Access methods: These methods are responsible for interacting with the database. They are called by the service methods and are responsible for sanitizing the input with prepared statements to prevent SQL injection attacks and returning the query result to the service layer.


### Connection Management

The project manages connections to a PostgreSQL database through a JDBC data source with `PGSimpleDataSource`.
It initializes a PGSimpleDataSource, acquires connections from it within the `main()`
function using a `connection.use { }` block for automatic closure, and encapsulates
database interactions within a SessionsDataManager instance.

### Data Access

For data access, we implemented the `SessionsDataManager` class, which is responsible for managing storage, be it in memory or in a database.

We also implemented the `SessionsDataGame`, `SessionsDataPlayer`, and `SessionsDataSession` interfaces, which are responsible for managing the data of the respective entities.
and support different types of implementations, such as in-memory storage or database storage.

For the storage interface implementation, we created the `SessionsDataGameDB`, `SessionsDataPlayerDB`, and `SessionsDataSessionDB` classes for database storage,
and the `SessionsDataGameMem`, `SessionsDataPlayerMem`, and `SessionsDataSessionMem` classes for in-memory storage.

### Error Handling/Processing

As said in the Request Details section, the application processes the request at the API level, which is responsible for catching any errors and returning the appropriate response.

In case of incorrect request parametrization, the application catches the exception and returns a 400 Bad Request response. This is because the API layer is responsible for validating the request parameters and checking for any errors.

In case of a database error, the application catches the exception and returns a 500 Internal Server Error response. This is
because the service layer is responsible for handling the business logic of the application and so the database methods should not be expected.
to throw exceptions.

To help with error handling for different types of errors, we created the `SessionsException` class, which is responsible for holding the status code, description, and cause of the error. This class is a throwable class
that can be extended by other classes in order to create custom exceptions and errors. These custom exceptions are then caught by the API layer and returned as an HTTP response.

These errors don't interrupt the application execution, as the application catches the exception and returns the appropriate HTTP response to the client.

## Critical Evaluation

As of the moment this report was written, the application is able to create, search, and add players to games and sessions.
All functionality for the first phase was implemented and tested.

No major defects were detected as of the time of writing this report, but some improvements can be made in the future:

- Increasing the test coverage of the application. This would help to ensure that the application is working as expected and that any changes made to the application do not break existing functionality. It
would also help idenfify any defects that may be present in the application and were not yet detected.

- Improving the way Data is managed in the application. Currently, the `SessionsDataManager` class is responsible for managing both memory and database storage with the same methods. 
This could be improved by adding some way to differentiate between memory and database storage, which could help separate concerns. 

- Adding a separate enum class for genres. Currently, to validate a genre in the application we use a list of strings. This could be improved by adding a separate enum class for genres, which would help with validation and make the code more readable.


