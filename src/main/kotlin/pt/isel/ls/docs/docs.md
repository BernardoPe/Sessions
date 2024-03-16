


## Sessions API
API for managing game sessions

### Version: 1.0.0

---

### /players

#### POST
##### Summary:

Create a new player

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Player created successfully |

---

### /players/{pid}

#### GET
##### Summary:

Get player details

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| pid | path |  | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Player details retrieved successfully |

---

### /games

#### POST
##### Summary:

Create a new game

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Game created successfully |

#### GET
##### Summary:

Get list of games

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | List of games retrieved successfully |

---

### /games/{gid}

#### GET
##### Summary:

Get game details

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gid | path |  | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Game details retrieved successfully |

---

### /sessions

#### POST
##### Summary:

Create a new session

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Session created successfully |

#### GET
##### Summary:

Get list of sessions

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | List of sessions retrieved successfully |

---

### /sessions/{sid}

#### GET
##### Summary:

Get session details

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| sid | path |  | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Session details retrieved successfully |

#### PUT
##### Summary:

Add player to session

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| sid | path |  | Yes | integer |
| pid | query |  | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Player added to session successfully |
