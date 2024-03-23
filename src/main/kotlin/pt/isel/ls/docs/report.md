# Phase 1

## Introduction

The first phase of the project consists of the development of an information system
to manage multiplayer game sessions.  
We implemented this first part of the project using `kotlin`,
the `HTTP4K` library for the web server and the `kotlinx.serialization` library for JSON serialization.  
We covered the implemented features with unit tests using the `JUnit` library.  
The database is managed by `PostgreSQL` and the connection is made using the `JDBC` library.

## Modeling the database

### Conceptual model ###

The following diagram holds the Entity-Relationship model for the information managed by the system.

(_include an image or a link to the conceptual diagram_)

We highlight the following aspects:

* (_include a list of relevant design issues_)

The conceptual model has the following restrictions:

* (_include a list of relevant design issues_)

### Physical Model ###

The physical model of the database is available in (_link to the SQL script with the schema definition_).

We highlight the following aspects of this model:

* (_include a list of relevant design issues_)

## Software organization

### Open-API Specification ###

[Open-api Specification](open-api.json)

In our Open-API specification, we highlight the following aspects:

We have 3 groups of endpoints: games, players, and sessions.  
The games group has 3 endpoints: one to create a game, search for games by name, developer and genre, and another to get games by id.
The players group has 2 endpoints: one to create a player and another to get players by id.
The sessions group has 4 endpoints: one to create a session, search for sessions by game id, player id, 
another to get sessions by id and another to add a player to a session.
We take advantage of the http error codes to inform the client of the errors that occurred.
Some examples of these are: 400 for bad request, 404 for not found, among others.


### Request Details

(_describe how a request goes through the different elements of your solution_)

(_describe the relevant classes/functions used internally in a request_)

(_describe how and where request parameters are validated_)

### Connection Management

The project manages connections to a PostgreSQL database through a JDBC data source with `PGSimpleDataSource`.
It initializes a PGSimpleDataSource, acquires connections from it within the `main()`
function using a `connection.use { }` block for automatic closure, and encapsulates
database interactions within a SessionsDataManager instance.

### Data Access

(_describe any created classes to help on data access_).

(_identify any non-trivial used SQL statements_).

### Error Handling/Processing

(_describe how errors are handled and their effects on the application behavior_).

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)