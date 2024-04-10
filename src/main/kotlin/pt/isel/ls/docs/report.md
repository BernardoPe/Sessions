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

[ER Diagram](ae_model_diagram.png)

We highlight the following aspects:

In this model, we have 3 entities: Game, Player, and Session.

Each game has a unique identifier, a unique name, a developer and a set of genres.

Each player has a unique identifier, a name, a unique e-mail and a token hash for authentication.

Each session has a unique identifier, a capacity, and a date.

The relationships between the entities are as follows:
- A game can have multiple sessions. A session can only have one game associated (1 to N). A session has to have a game associated to it to exist.
- A player can be associated with multiple sessions. A session can have multiple players associated (N to N). A player does not have to be associated with a session to exist.
- A session does not have to have any players associated with it to exist.

The conceptual model has the following restrictions:
- Ids must be valid numbers.
- Genres must be valid strings and belong to a set of predefined genres.
- Names must be valid strings.
- Emails must follow a valid email format.
- Dates must follow a valid date format.
- Sessions must have a capacity greater than 0 and at most 100.
- Sessions must have a date in the future.
- To add a player to a session, the player must not already be in the session, the session must not be full and not closed
(current date must be before the session date).


### Physical Model ###


The physical model of the database is available [here](../../../../../sql/createTables.sql).

We highlight the following aspects of this model:

The database has 3 tables: Games, Players, and Sessions.

In this model, a sessions_players table was created to represent the N to N relationship between players and sessions.
This table holds the foreign keys to the Players and Sessions tables, holding a Session-Player association pair in each row.
Additionally, the foreign key pair is defined as the primary key of the table, ensuring that a player can only be associated with a session once.

IDs were defined as serial primary keys. The Sessions table holds a foreign key to the Games table, representing the 1 to N relationship between games and sessions.

The genres defined as valid were 'Action', 'Adventure', 'RPG', 'Strategy', 'Turn-Based'.

Data Integrity restrictions mentioned in the conceptual model are enforced by the database by defining the proper types and constraints for each column.

The remaining restrictions mentioned in the conceptual model that are
not present in the physical model are enforced by the application logic at the service layer.

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

The Sessions group has 7 endpoints: 
 - Create Session : POST /sessions
 - Update session : PUT /sessions/{id}
 - Delete session : DELETE /sessions/{id}
 - Session Search : GET /sessions/{gid}/list
 - Session Search by id : GET /sessions/{id}
 - Add player to session : POST /sessions/{id}/players
 - Remove player from session : DELETE /sessions/{id}/players/{pid}


Response codes are:

| Response Code           | Description           | Examples                           |
|-------------------------|-----------------------|------------------------------------|
| 200                     | OK                    | Got a Player                       |
| 201                     | Created               | Create a Game                      |
| 400                     | Bad Request           | Missing a parameter on the request |
| 404                     | Not Found             | Player not found                   |
| 409                     | Conflict              | The Player already exists          |
| 500                     | Internal Server Error | An error in the server             |

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

We also implemented the `SessionsDataGame`, `SessionsDataPlayer`, and `SessionsDataSession` interfaces, which are responsible for managing the data of the respective entities and support different types of implementations, such as in-memory storage or database storage.

For the storage interface implementation, we created the `SessionsDataGameDB`, `SessionsDataPlayerDB`, and `SessionsDataSessionDB` classes for database storage,
and the `SessionsDataGameMem`, `SessionsDataPlayerMem`, and `SessionsDataSessionMem` classes for in-memory storage.

### Error Handling/Processing
   #### Back-End 
As said in the Request Details section, the application processes the request at the API level, which is responsible for catching any errors and returning the appropriate response.

In case of incorrect request parametrization, the application catches the exception and returns a 400 Bad Request response. This is because the API layer is responsible for validating the request parameters and checking for any errors.

In case of a database error, the application catches the exception and returns a 500 Internal Server Error response. This is
because the service layer is responsible for handling the business logic of the application and so the database methods should not be expected
to throw exceptions.

To help with error handling for different types of errors, we created the `SessionsException` class, which is responsible for holding the status code, description, and cause of the error. This class is a throwable class
that can be extended by other classes in order to create custom exceptions and errors. These custom exceptions are then caught by the API layer and returned as an HTTP response.

These errors don't interrupt the application execution, as the application catches the exception and returns the appropriate HTTP response to the client.
   #### Front-End
The SPA application prevents the user from committing errors by using DOM and CSS to sanitize user input and validate the input fields before sending the request to the server.

If a Back-End error is relevant to the user, the application displays an error message to the user, informing them of the error.

An example would be if no results are found for a given search, the application informs the user that no results were found and that they should try again with different parameters.

For pagination, the SPA tries to fetch the next page of results, but if no results are found, the application doesn't show the button to fetch the next page.

## Critical Evaluation

As of the moment this report was written, the application is able to create, search, and add players to games and sessions.

No major defects were detected as of the time of writing this report.

The only currently planned change is to increase the API test coverage with more edge cases.
