# Relational Model

The relational model is a way to represent data in a tabular form. The model is based on the concept of a relation,
which is a table with rows and columns. Each row in a relation represents a record, and each column represents an
attribute of the record. The relational model is widely used in database management systems (DBMS) to store and
manipulate data.

## Relations

### Games

- id
- name
- developer
- genres

PK: id

AK: name, genres

### Players

- id
- name
- email
- token_hash

PK: id

AK: email, token_hash

### Sessions

- id
- capacity
- date
- gameSession
- playersSession

PK: id

FK: gameSession -> Games.id
    playersSession -> Players.id
