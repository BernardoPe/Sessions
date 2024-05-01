import {a, br, button, div, fieldset, h1, input, label, legend, p, ul} from "../../WebDSL/web_dsl.js";
import {GAMES_URL, PLAYERS_URL, SESSIONS_URL} from "../../../index.js";
import { handleSearch, showSearchResults, resultsKeyHandler, hideSearchResults} from "../../Scripts/search.js";

window.handleSearch = handleSearch;
window.showSearchResults = showSearchResults;
window.resultsKeyPressHandler = resultsKeyHandler;
window.hideSearchResults = hideSearchResults;

/**
 * Returns an HTML structure for a form input field
 * @param id - id of the input field
 * @param name - name of the input field
 * @param type - type of the input field
 * @param title - title of the input field
 */
function formInputField(id, name, type, title) {
	return div({class: "form__input"},
		input({
			id: id,
			class: "form__field",
			type: type,
			placeholder: "",
			name: name,
			autocomplete: "off"
		}),
		label(title, {class:"form__label"}, title),
	)
}

/**
 * Returns an HTML structure for a game's details
 * @param game - game object
 */

function gameDetails(game) {
	return div(null,
		div({class:"game-container"},
			h1({class:"game__title"}, game.name),
			p({class:"game__developer"}, "By " + game.developer),
			p({class:"game__genres"}, "Genres: " + game.genres.join(', ')),
		),
		a(`#` + `${SESSIONS_URL}` + `?gid=${game.gid}`, {class:"search-ref"}, "Sessions with this game")
	)
}


/**
 * Returns an HTML structure for a player's details
 * @param player - player object
 */

function playerDetails(player) {
	return div(null,
		div({class: "player-container"},
			p({class: "player__name"}, player.name),
			p({class: "player__email"}, "Contact info: " + player.email)
		),
		a(`#` + SESSIONS_URL + `?pid=${player.pid}`, {class: "search-ref"}, "Your sessions")
	)
}

/**
 * Returns an HTML structure for a session's details
 * @param session - session object
 */
function removePlayerFromSession(sid, pid) {
	const confirmRemove = confirm("Are you sure you want to remove this player from the session?");
	if (!confirmRemove) {
		return;
	}
	const errMessage = document.getElementById("err_message-remove_player");

	errMessage.style.display = 'none';

	fetch(API_URL + SESSIONS_URL + `/${sid}/players/${pid}`, {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json"
		}
	})
		.then(res => {
			res.json()
				.then(data => {
					if (res.ok) {
						location.reload();
						console.log(data);
						window.location.href = `#sessions/${sid}`;
					} else {
						return Promise.reject(res)
					}
				})
				.catch(error => {
					errMessage.innerHTML = error.errorCause
					errMessage.style.display = "block"
				});
		});
}

function deleteSession(sid) {
	const confirmDelete = confirm("Are you sure you want to delete this session?");
	if (!confirmDelete) {
		return;
	}
	// const errMessage = document.getElementById("err_message-delete_session");
	//
	// errMessage.style.display = 'none';

	fetch(API_URL + SESSIONS_URL + `/${sid}`, {
		method: "DELETE",
		headers: {
			"Content-Type": "application/json"
		}
	})
		.then(res => {
			res.json()
				.then(data => {
					if (res.ok) {
						location.reload();
						console.log(data);
						window.location.href = `#home`;
					} else {
						return Promise.reject(res)
					}
				})
			// .catch(error => {
			// 	console.log(error) // this may not work
			// 	errMessage.innerHTML = error.errorCause
			// 	errMessage.style.display = "block"
			// });
		});
}

function sessionDetails(session) {
	return div({class:"session-container"},
		p({class:"session__game"},
			a(`#` + `${GAMES_URL}/` + `${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class:"session__date"}, session.date),
		fieldset({class:"session__players"},
			legend(null, "Players " + session.playersSession.length + "/" + session.capacity),
			...session.playersSession.map(
				player =>
					div({class:"session__player"},
						a(`#` + `${PLAYERS_URL}/` + `${player.pid}`, null, player.name),
						button({
							class: "session__player__remove",
							id: "remove_player",
							type: "button",
							onclick: `removePlayerFromSession(${session.sid}, ${player.pid})`
						}, "Remove Player"),
						errorMessage("err_message-remove_player", "Error removing player from session")
					)
			)
		),
		p({class: "session__add__player"} // Must create a css style class for this
			//TODO: add player to session form
		),
		p({class: "session__delete"},
			button({
				class: "session__delete__button",
				id: "delete_session",
				type: "button",
				onclick: `deleteSession(${session.sid})`
			}, "Delete Session"),
			//errorMessage("err_message-delete_session", "Error deleting session") // maybe this is not needed
		),
	)
}


/**
 * Returns an HTML structure for a form input field with search results
 * @param id - id of the input field
 *
 * @param searchType - type of the search. This is used to determine the name search endpoint to call from the API.
 *
 * @param fieldType - type of the input field
 *
 * @param title - title of the input field
 */

function formInputWithSearchResults(id, searchType, fieldType, title) {
	return div({class: "form__input"},
		input({
			id: id,
			class: "form__field",
			type: fieldType,
			placeholder: "",
			name: searchType,
			autocomplete: "off",
			oninput: "handleSearch(event, id, name)",
			oncut: "handleSearch(event, id, name)",
			onpaste: "handleSearch(event, id, name)",
			onfocus: "showSearchResults(id); handleSearch(event, id, name)",
			onblur: "setTimeout(() => hideSearchResults(id), 100)", // delay to allow click on search result
			onkeydown: "resultsKeyPressHandler(event, id)",
		}),
		label(title, {class:"form__label"}, title),
		ul({class: "search_results", id: "search_results_" + id }, div({class: "search-results"})),
	)
}

/**
 * Returns an HTML structure for a game search result
 * @param game - game object
 */

function gameSearchResult(game) {
	return div({class: "game-container"},
		p({class: "game__title"},
			a(`#games/${game.gid}`, null, game.name)
		),
		p({class: "game__developer"}, game.developer),
		br(null)
	)
}

/**
 * Returns an HTML structure for a genre input field
 */

function genresInput() {
	return fieldset(null,
		legend(null, "Select Genres:"),
		input({id: "RPG", type: "checkbox", name: "genre", value: "RPG"}),
		label("RPG", null, "RPG"),
		br(null),
		input({id: "Adventure", type: "checkbox", name: "genre", value: "Adventure"}),
		label("Adventure", null, "Adventure"),
		br(null),
		input({id: "Shooter", type: "checkbox", name: "genre", value: "Shooter"}),
		label("Shooter", null, "Shooter"),
		br(null),
		input({id: "TurnBased", type: "checkbox", name: "genre", value: "Turn-Based"}),
		label("TurnBased", null, "TurnBased"),
		br(null),
		input({id: "Action", type: "checkbox", name: "genre", value: "Action"}),
		label("Action", null, "Action"),
		br(null),
	)
}

/**
 * Returns an HTML structure for a session state input field
 */
function sessionStateInput() {
	return fieldset(null,
		legend(null, "Session state:"),
		input({type: "radio", id: "state1", name: "state", value: "OPEN"}),
		label("state1", null, "Open"),
		br(null),
		input({type: "radio", id: "state2", name: "state", value: "CLOSE"}),
		label("state2", null, "Closed"),
		br(null),
	)
}


/**
 * Returns an HTML structure for a session search result
 */

function sessionSearchResult(session) {
	return div({class: "session-container"},
		p({class: "session__game"},
			a(`#games/${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class: "session__date"}, session.date),
		a(`#sessions/${session.sid}`, {class: "session__reference"}, "Get more details"),
		br(null)
	)
}

/**
 * Returns an HTML structure for a date time input field
 */
function dateTimeInput() {
	return div({class: "form__input"},
		input({id: "date", type: "datetime-local", class:"form__field datepicker", placeholder: "yyyy-mm-dd", name: "date", value: ""}),
		label("date", {class:"form__label"}, "Date"),
	)
}


/**
 * Returns an HTML structure for an error message
 */

function errorMessage(id, msg) {
	return div({id: id, class: "error-message-container"},
		p({class: "error_message"}, msg)
	)
}


export {
	formInputField,
	formInputWithSearchResults,
	errorMessage,
	genresInput,
	sessionStateInput,
	dateTimeInput,
	sessionSearchResult,
	gameSearchResult,
	gameDetails,
	playerDetails,
	sessionDetails
}