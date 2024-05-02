import {a, br, button, div, fieldset, form, i, legend, p} from "../../../WebDSL/web_dsl.js";
import {GAMES_URL} from "../../../../index.js";
import {sessionPlayer} from "../Players/players.js";
import {errorMessage} from "../Errors/error.js";
import {dateTimeInput} from "../Inputs/date.js";
import {formInputField, formInputWithSearchResults} from "../Inputs/form.js";
import {confirmationDiv} from "../Inputs/confirm.js";

/**
 * Returns an HTML structure for a session's details
 * @param session - session object
 */
function sessionDetails(session) {
	return div({class:"session-container fade-up"},
		p({class:"session__game"},
			a(`#` + `${GAMES_URL}/` + `${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class:"session__date"}, formatDate(session.date)),
		fieldset({class:"session__players"},
			legend({id:"session-capacity"}, "Players " + session.playersSession.length + "/" + session.capacity),
			...session.playersSession.map(
				player => sessionPlayer(session.sid, player, false)
			)
		),
	)
}

/**
 * Returns an HTML structure for a session's details with the additional authenticated actions
 */
function sessionDetailsAuthenticated(session) {

	const sessionPlayers = fieldset({class:"session__players"},
		legend({id:"session-capacity"}, "Players " + session.playersSession.length + "/" + session.capacity),
		div({class: "session__players__list"},
			...session.playersSession.map(
				player => sessionPlayer(session.sid, player, true)
			)
		),
	)
	const sessionView = div({class:"session-container"},
		p({class:"session__game"},
			a(`#` + `${GAMES_URL}/` + `${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class:"session__date"}, formatDate(session.date)),
		button({
				class: "icon__button",
				id: "delete-session",
				onclick: `handleConfirmation(event, id, function() { deleteSession(event, ${session.sid}) })`
			},
			i({class: "fas fa-solid fa-trash fa-2x red"}),
		),
		sessionPlayers
	)
	if (new Date(session.date) > new Date())  {
		sessionPlayers.appendChild(
			button({
					class: "icon__button",
					id: "add-player",
					onclick: `createPlayerAddForm(event, ${session.sid}, id)`
				},
				i({class: "fas fa-plus fa-2x green"})
			)
		)
	}
	sessionView.appendChild(
		div({class: "session__update"},
			form({onsubmit: `submitFormUpdateSession(event, ${session.sid})`},
				formInputField(
					"capacity",
					"capacity",
					"number",
					"Session capacity"
				),
				errorMessage("err_message-capacity", "Capacity must be a number"),
				dateTimeInput(),
				errorMessage("err_message-date", "Invalid date to create a session"),
				button({type: "submit"}, "Edit Session"),
			)
		)
	)
	return sessionView
}

/**
 * Returns an HTML structure for a session search result
 */

function sessionSearchResult(session) {
	return div({class: "session-container"},
		p({class: "session__game"},
			a(`#games/${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class: "session__date"}, formatDate(session.date)),
		a(`#sessions/${session.sid}`, {class: "session__reference"}, "Get more details"),
		br(null)
	)
}


function handleConfirmation(event, id, action) {
	event.preventDefault()
	const div = document.getElementById(id)
	const confirmation = confirmationDiv(action, id)
	div.style.display = "none"
	div.parentNode.insertBefore(confirmation, div)
}

function createPlayerAddForm(event, sid, id) {
	event.preventDefault()
	const addBtn = document.getElementById(id)
	addBtn.style.display = "none"
	const playerAddForm = div({class: "session__add__player fade-in"},
		form({onsubmit: `submitFormSessionAddPlayer(event, ${sid})`},
			formInputWithSearchResults(
				"player",
				"players",
				"text",
				"Player name"
			),
			errorMessage("err_message-player", "Player name must be at least 3 characters long"),
			button({type: "submit"}, "Add Player"),
		)
	)
	addBtn.parentNode.insertBefore(playerAddForm, addBtn)
}

window.handleConfirmation = handleConfirmation
window.createPlayerAddForm = createPlayerAddForm

/**
 * Formats the date to a presentable string
 * @param date - date object
 */

function formatDate(date) {
	const dateObject = new Date(date);
	const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
	return dateObject.toLocaleDateString(undefined, options);
}


export {sessionDetails, sessionDetailsAuthenticated, sessionSearchResult, formatDate}