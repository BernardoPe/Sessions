import {API_URL} from "../../index.js";

const SESSION_MAX_CAPACITY = 100;

window.submitFormGameSearch = submitFormGameSearch;
window.submitFormSessionSearch = submitFormSessionSearch;
window.submitFormCreateGame = submitFormCreateGame;
window.submitFormCreateSession = submitFormCreateSession;

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
		if (handleNameInput(gameName, errMessageGame) === undefined)
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
		if (handleNameInput(playerName, errMessagePlayer) === undefined)
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

	if (isTextNotInserted(name, 'game', gameErr))
		return;

	if (!handleNameInput(name, gameErr))
		return;

	if (isTextNotInserted(developer, 'developer', developerErr))
		return;

	if (!handleNameInput(developer, developerErr))
		return;

	if (genres.length === 0) {
		genreErr.style.display = 'block';
		genreErr.innerHTML = 'At least one genre must be selected';
		return;
	}

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

	if (capacity < 1) {
		capacityErr.style.display = 'block';
		capacityErr.innerHTML = 'Session capacity must be at least 1';
		return;
	}

	if (capacity > SESSION_MAX_CAPACITY) {
		capacityErr.style.display = 'block';
		capacityErr.innerHTML = 'Session capacity must be at most ' + SESSION_MAX_CAPACITY;
		return;
	}

	if (date === '') {
		dateErr.style.display = 'block';
		dateErr.innerHTML = 'Please enter a date for the session';
		return;
	}

	const dateDOMToDate = new Date(date);

	if (dateDOMToDate < new Date()) {
		dateErr.style.display = 'block';
		dateErr.innerHTML = 'Date of the session must be in the future';
		return;
	}

	if (isTextNotInserted(gameName, 'game', gameNameErr))
		return;

	if (handleNameInput(gameName, gameNameErr) === undefined)
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

function isTextNotInserted(name, type, err) {
	if (name === '') {
		err.style.display = 'block';
		err.innerHTML = 'Please enter a name for a ' + type;
		return true;
	} else return false;
}

function getUniqueGameId(gameName) {
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




export {submitFormGameSearch, submitFormSessionSearch, submitFormCreateGame, submitFormCreateSession};