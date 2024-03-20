# Phase 1

## Introduction

This document contains the relevant design and implementation aspects of LS project's first phase.

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

(_include a list of relevant issues or details in your specification_)

### Request Details

(_describe how a request goes through the different elements of your solution_)

(_describe the relevant classes/functions used internally in a request_)

(_describe how and where request parameters are validated_)

### Connection Management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

### Data Access

(_describe any created classes to help on data access_).

(_identify any non-trivial used SQL statements_).

### Error Handling/Processing

(_describe how errors are handled and their effects on the application behavior_).

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)