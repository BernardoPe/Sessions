import {API_URL} from "../../index.js";

const SESSION_MAX_CAPACITY = 100;


/**
 * Handles the search operation for games
 *
 * To search for games, the user can input the name of the game, the developer of the game, and the genres of the game.
 *
 * In case the user searches by game name, the game name must be at least 3 characters long, and it must be an exact match
 * to an existing game name in the Application's database.
 *
 * All of these fields are optional, and the user can search for games by any combination of these fields, or by none of them.
 */
function submitFormGameSearch(event) {
	event.preventDefault()
	const developer = document.getElementById('developer').value;
	const name = document.getElementById('game').value;
	const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
	const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value).join(',');
	const errMessageGame = document.getElementById('err_message-game');
	const errMessageDeveloper = document.getElementById('err_message-developer');
	let queries = new URLSearchParams();

	if (name !== '') {
		console.log(name)
		if (!handleNameInput(name, errMessageGame))
			return;
		queries.append('name', name);
	}

	if (developer !== '') {
		if (!handleNameInput(developer, errMessageDeveloper))
			return;
		queries.append('developer', developer);
	}

	if (genres) {
		queries.append('genres', genres);
	}

	if (queries.size > 0) {
		window.location.href = `#games?${queries}`;
	}
	else
		window.location.href = `#games`;

}

/**
 * Handles the search operation for sessions
 *
 * To search for sessions, the user can input the name of the game, the name of the player, the state of the session, and the date of the session.
 *
 * In case the user searches by game or player name, the name must be at least 3 characters long, and it must
 * have an exact match to an existing game or player name in the Application's database.
 *
 * The state of the session can be 'open' or 'closed', and the date of the session must be in the future.
 *
 * All of these fields are optional, and the user can search for sessions by any combination of these fields, or by none of them.
 *
 */
async function submitFormSessionSearch(event) {

	event.preventDefault();
	const gameName = document.getElementById('game').value;
	const playerName = document.getElementById('player').value;
	const stateElement = document.querySelector('input[name="state"]:checked');
	const state = stateElement ? stateElement.value : null;
	const date = document.getElementById('date').value;
	const errMessageGame = document.getElementById('err_message-game');
	const errMessagePlayer = document.getElementById('err_message-player');
	let queries = new URLSearchParams();

	if (gameName.length > 0) {
		if (!handleNameInput(gameName, errMessageGame))
			return;
		const gid = await getUniqueGameId(gameName);
		if (gid) {
			queries.append('gid', gid)
		} else {
			errMessageGame.style.display = 'block';
			errMessageGame.innerHTML = 'No game found with that name';
			return
		}
	}

	if (playerName.length > 0) {
		if (!handleNameInput(playerName, errMessagePlayer))
			return;
		const pid = await getUniquePlayerId(playerName);
		if (pid) {
			queries.append('pid', pid)
		} else {
			errMessagePlayer.style.display = 'block';
			errMessagePlayer.innerHTML = 'No player found with that name';
			return
		}
	}

	if (state) {
		queries.append('state', state);
	}

	if (date) {
		queries.append('date', date);
	}

	if (queries.length > 0)
		queries = queries.slice(0, queries.length - 1); // remove trailing '&'

	if (queries.toString().length > 0)
		window.location.href = `#sessions?${queries}`;
	else
		window.location.href = `#sessions`;
}

/**
 * Handles the creation of a new game
 *
 * To create a new game, the user must input the name of the game, the name of the developer, and select at least one genre.
 *
 * The name of the game and the developer must be at least 3 characters long.
 *
 * If any errors occur during the creation of the game, an error message will be displayed to the user, informing them of the issue.
 *
 */
function submitFormCreateGame(event) {

	event.preventDefault();
	const name = document.getElementById('game_name').value;
	const developer = document.getElementById('developer_name').value;
	const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
	const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value);
	const gameErr = document.getElementById('err_message-game');
	const developerErr = document.getElementById('err_message-developer');
	const genreErr = document.getElementById('err_message-genres');

	gameErr.style.display = 'none';
	developerErr.style.display = 'none';
	genreErr.style.display = 'none';

	if (
		isInputNotInserted(name, 'game', gameErr) ||
		isInputNotInserted(developer, 'developer', developerErr) ||
		isInputNotInserted(genres, 'genre', genreErr) ||
		!handleNameInput(name, gameErr) ||
		!handleNameInput(developer, developerErr)
	) return;

	fetch(API_URL + `games`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({name, developer, genres})
	})
	.then(res => res.status === 201 ? res.json() : Promise.reject(res))
	.then(data => {
		const gid = data.gid;
		window.location.href = `#games/${gid}`;
	})
	.catch(err => {
		err.json().then(err => {
			gameErr.innerHTML = err.errorCause
			gameErr.style.display = 'block';
		})
	})
}

/**
 * Handles the creation of a new session
 *
 * To create a new session, the user must input the capacity of the session, the date of the session, and the name of the game.
 *
 * The capacity of the session must be at least 1 and at most 100.
 *
 * The date of the session must be in the future.
 *
 * The name of the game must be at least 3 characters long, and it must be an exact match to an existing game name in the Application's database.
 *
 * If any of the above conditions are not met, an error message will be displayed to the user, informing them of the issue.
 */
async function submitFormCreateSession(event) {
	event.preventDefault();
	const capacity = document.getElementById('capacity').value;
	const date = document.getElementById('date').value;
	const gameName = document.getElementById('game_name').value;
	const capacityErr = document.getElementById('err_message-capacity');
	const dateErr = document.getElementById('err_message-date');
	const gameNameErr = document.getElementById('err_message-game');

	capacityErr.style.display = 'none';
	dateErr.style.display = 'none';
	gameNameErr.style.display = 'none';

	if (
		!handleGameCapacity(capacity, capacityErr) ||
		!handleDateInput(date, dateErr ||
		!handleNameInput(gameName, gameNameErr))
	)
		return;

	const gid = await getUniqueGameId(gameName);

	if (!gid) {
		const err = document.getElementById('err_message-game');
		err.style.display = 'block';
		err.innerHTML = 'No game found with that name';
		return
	}

	fetch(API_URL + `sessions`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({capacity, date, gid})
	})
		.then(res => res.json())
		.then(data => {
			const sid = data.sid;
			window.location.href = `#sessions/${sid}`;
		})
		.catch(err => {
			err.json().then(err => {
				if (err.errorCause.toLowerCase().includes("date")) {
					dateErr.innerHTML = err.errorCause
					dateErr.style.display = "block"
				} else {
					gameNameErr.innerHTML = err.errorCause
					gameNameErr.style.display = "block"
				}
			})
		})
}

async function submitFormSessionAddPlayer(event, sid) {
	event.preventDefault();
	const playerName = document.getElementById('player').value;
	const playerNameErr = document.getElementById('err_message-player');

	playerNameErr.style.display = 'none';

	if (
		isInputNotInserted(playerName, 'player', playerNameErr) ||
		!handleNameInput(playerName, playerNameErr)
	) return;

	const pid = await getUniquePlayerId(playerName);
	if (!pid) {
		const err = document.getElementById('err_message-player');
		err.style.display = 'block';
		err.innerHTML = 'No player found with that name';
		return
	}

	try { // Try and Catch may not be needed
		const response = await fetch(API_URL + `sessions/${sid}/players`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({pid})
		});

		if (response.status === 201) {
			await response.json();
			/** DOUBT:
			 * Doing "window.location.href = `#sessions/${sid}`;" won´t refresh the session with the added player
			 */
			location.reload();
		} else {
			const error = await response.json();
			playerNameErr.innerHTML = error.errorCause;
			playerNameErr.style.display = "block";
		}
	} catch (error) {
		console.error(error);
	}
}

/**
 * Handles the input of a name
 *
 * The name must be at least 3 characters long
 *
 * @param name - the name to be checked
 * @param errMessage - the error message to be displayed in case the name is invalid
 */
function handleNameInput(name, errMessage) {
	if (name.length < 3) {
		errMessage.style.display = 'block';
		errMessage.innerHTML = 'Name must be at least 3 characters long';
		return undefined;
	} else {
		errMessage.style.display = 'none';
		return name;
	}
}


/**
 * Checks if the text is not inserted
 * @param input - the input to be checked
 * @param type - the type of the input
 * @param err - the error message to be displayed in case the input is invalid
 */

function isInputNotInserted(input, type, err) {
	if (input.length === 0) {
		err.style.display = 'block';
		err.innerHTML = 'Please enter a name for a ' + type;
		return true;
	} else return false;
}

/**
 * Tries to get the unique game id from the database based on the game name
 * @param gameName - the name of the game
 */
function getUniqueGameId(gameName) {
	console.log(gameName)
	return fetch(API_URL + `games?name=${gameName}`)
		.then(response => response.status === 200 ? response.json() : Promise.reject(response))
		.then(res => {
			const games = res.games
			if (games.length === 0) {
				return false;
			}
			const gameMatch = games.find(game => game.name === gameName);
			return gameMatch.gid;
		}).catch(res => {
			return false;
		})
}

/**
 * Tries to get the unique player id from the database based on the player name
 * @param playerName
 */
function getUniquePlayerId(playerName) {
	return fetch(API_URL + `players?name=${playerName}`)
		.then(response => response.status === 200 ? response.json() : Promise.reject(response))
		.then(res => {
			const players = res.players;
			const player = players.find(player => player.name === playerName);
			return player.pid;
		}).catch(res => {
			return false;
		})
}

/**
 * Handles the capacity of the game
 *
 * The capacity of the game must be at least 1 and at most 100
 * @param capacity - the capacity to be checked
 * @param errMessage - the error message to be displayed in case the capacity is invalid
 */
function handleGameCapacity(capacity, errMessage) {
	if (capacity < 1 || capacity > SESSION_MAX_CAPACITY) {
		errMessage.style.display = 'block';
		errMessage.innerHTML = 'Capacity must be between 1 and 100';
		return false;
	} else {
		errMessage.style.display = 'none';
		return true;
	}
}


/**
 * Handles the date input
 *
 * The date must be in the future
 *
 * @param date - the date to be checked
 * @param errMessage - the error message to be displayed in case the date is invalid
 */
function handleDateInput(date, errMessage) {
	const currentDate = new Date();
	if (date === '') {
		errMessage.style.display = 'block';
		errMessage.innerHTML = 'Please enter a date';
		return false;
	}
	const inputDate = new Date(date);
	if (inputDate < currentDate) {
		errMessage.style.display = 'block';
		errMessage.innerHTML = 'Date must be in the future';
		return false;
	}
	errMessage.style.display = 'none';
	return true;
}

window.submitFormGameSearch = submitFormGameSearch;
window.submitFormSessionSearch = submitFormSessionSearch;
window.submitFormCreateGame = submitFormCreateGame;
window.submitFormCreateSession = submitFormCreateSession;
window.submitFormSessionAddPlayer = submitFormSessionAddPlayer;

export {
	submitFormGameSearch,
	submitFormSessionSearch,
	submitFormCreateGame,
	submitFormCreateSession,
	submitFormSessionAddPlayer
};