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
| 400 | Invalid body |
| 409 | Conflict |

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
| 400 | Bad Request |
| 404 | Not found |

---

### /games

#### POST
##### Summary:

Create a new game

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Game created successfully |
| 400 | Bad Request |
| 401 | Unauthorized |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

#### GET
##### Summary:

Get list of games

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| limit | query |  | No | int |
| skip | query |  | No | int |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | List of games retrieved successfully |
| 400 | Bad Request |


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
| 400 | Bad Request |
| 404 | Not found |

---

### /sessions

#### POST
##### Summary:

Create a new session

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Session created successfully |
| 400 | Bad Request |
| 401 | Unauthorized |
| 404 | Not found |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |

#### GET
##### Summary:

Get list of sessions

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| limit | query |  | No | int |
| skip | query |  | No | int |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | List of sessions retrieved successfully |
| 400 | Bad Request |

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
| 400 | Bad Request |
| 404 | Not found |

---

### /sessions/{sid}/players

#### PUT
##### Summary:

Add player to session

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| sid | path |  | Yes | integer |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Player added to session successfully |
| 400 | Bad Request |
| 401 | Unauthorized |
| 404 | Not found |

##### Security

| Security Schema | Scopes |
| --- | --- |
| bearerAuth | |
