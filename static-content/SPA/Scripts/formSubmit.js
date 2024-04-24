function submitFormGameSearch(event) {
	event.preventDefault();
	const developer = document.getElementById('developer').value;
	const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
	const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value).join(',');

	let queries = '';

	if (developer) {
		queries += `developer=${developer}&`;
	}

	if (genres) {
		queries += `genres=${genres}&`;
	}

	if (queries.length > 0)
		queries = queries.slice(0, queries.length - 1); // remove trailing '&'

	if (queries.toString().length > 0)
		window.location.href = `#games/searchResults?${queries}`;
	else
		window.location.href = `#games/searchResults`;

}

function submitFormSessionSearch(event) {
	event.preventDefault();
	const gameId = document.getElementById('game').value;
	const playerId = document.getElementById('player').value;
	const stateElement = document.querySelector('input[name="state"]:checked');
	const state = stateElement ? stateElement.value : null;
	const date = document.getElementById('date').value.replace(':', '_');

	let queries = '';

	if (gameId) {
		queries += `gid=${gameId}&`;
	}

	if (playerId) {
		queries += `pid=${playerId}&`;
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
		window.location.href = `#sessions/searchResults?${queries}`;
	else
		window.location.href = `#sessions/searchResults`;
}

function submitFormCreateGame(event) {
	// TODO: implement
}

function submitFormCreateSession(event) {
	// TODO: implement
}

export {submitFormGameSearch, submitFormSessionSearch, submitFormCreateGame, submitFormCreateSession};