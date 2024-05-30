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
- password_hash

PK: id

AK: email, name, password_hash

### Tokens

- token
- player_id
- time_expiration
- time_creation

PK: token

FK: player_id -> Players.id

### Sessions

- id
- capacity
- date
- game_id

PK: id

FK: game_id -> Games.id


### Sessions_Players

- session_id
- player_id

PK: session_id, player_id

