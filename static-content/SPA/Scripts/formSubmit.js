function submitFormGameSearch(event) {
	event.preventDefault();
	const developer = document.getElementById('developer').value;
	const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
	if (checkedCheckboxes.length === 0) {
		const genreFieldSet = document.querySelector('fieldset');
		const errorMessageGenre = document.getElementById('err_message');
		errorMessageGenre.style.display = "flex"
		genreFieldSet.style.borderColor = "yellow"
		return
	}
	const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value).join(',');
	window.location.href = `#games/searchResults?developer=${developer}&genres=${genres}`;
}

function submitFormSessionSearch(event) {
	event.preventDefault();
	const gameId = document.getElementById('game').value;
	const playerId = document.getElementById('player').value;
	const stateElement = document.querySelector('input[name="state"]:checked');
	const state = stateElement ? stateElement.value : null;
	const date = document.getElementById('date').value;

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

	queries = queries.slice(0, queries.length - 1); // remove trailing '&'

	if (queries.toString().length > 0)
		window.location.href = `#sessions/searchResults?${queries}`;
	else
		window.location.href = `#sessions/searchResults`;
}

export { submitFormGameSearch, submitFormSessionSearch };