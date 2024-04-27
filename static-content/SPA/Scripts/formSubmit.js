import {API_URL} from "../../index.js";

function submitFormGameSearch(event) {
	event.preventDefault()
	const developer = document.getElementById('developer').value;
	const name = document.getElementById('game').value;
	const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
	const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value).join(',');

	let queries = '';

	if (name !== '') {
		if (handleNameInput(name, 'game') === undefined)
			return;
		queries += `name=${name}&`;
	}

	if (developer !== '') {
		if (handleNameInput(developer, 'developer') === undefined)
			return;
		queries += `developer=${developer}&`;
	}

	if (genres) {
		queries += `genres=${genres}&`;
	}

	if (queries.length > 0)
		queries = queries.slice(0, queries.length - 1); // remove trailing '&'

	if (queries.toString().length > 0) {
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
	const date = document.getElementById('date').value.replace(':', '_');

	let queries = '';

	if (gameName.length > 0) {
		if (handleNameInput(gameName, 'game') === undefined)
			return;
		const gid = await getUniqueGameId(gameName);
		if (gid) {
			queries += `gid=${gid}&`;
		} else {
			const err = document.getElementById('err_message-game');
			err.style.display = 'block';
			err.innerHTML = 'No game found with that name';
			return
		}
	}

	if (playerName.length > 0) {
		if (handleNameInput(playerName, 'player') === undefined)
			return;
		const pid = await getUniquePlayerId(playerName);
		if (pid) {
			queries += `pid=${pid}&`;
		} else {
			const err = document.getElementById('err_message-player');
			err.style.display = 'block';
			err.innerHTML = 'No player found with that name';
			return
		}
	}

	if (state) {
		queries += `state=${state}&`;
	}

	if (date) {
		queries += `date=${date}&`;
	}

	if (queries.length > 0)
		queries = queries.slice(0, queries.length - 1); // remove trailing '&'

	if (queries.toString().length > 0)
		window.location.href = `#sessions?${queries}`;
	else
		window.location.href = `#sessions`;
}

function handleNameInput(name, type) {
	const err = document.getElementById('err_message-' + type)
	if (name.length < 3) {
		err.style.display = 'block';
		err.innerHTML = 'Name must be at least 3 characters long';
		return undefined;
	} else {
		err.style.display = 'none';
		return name;
	}
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


function submitFormCreateGame(event) {
	// TODO: implement
}

function submitFormCreateSession(event) {
	// TODO: implement
}

export {submitFormGameSearch, submitFormSessionSearch, submitFormCreateGame, submitFormCreateSession};