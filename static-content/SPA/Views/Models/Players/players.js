
import {a, button, div, i, p} from "../../../WebDSL/web_dsl.js";
import {PLAYERS_URL, SESSIONS_URL} from "../../../../index.js";
import {handleConfirmation} from "../Sessions/sessions.js";

/**
 * Returns an HTML structure for a player's details
 * @param player - player object
 */
function playerDetails(player) {
	return div(null,
		div({class: "player-container fade-up"},
			p({class: "player__name"}, player.name),
			p({class: "player__email"}, "Contact info: " + player.email)
		),
		a(`#` + SESSIONS_URL + `?pid=${player.pid}`, {class: "search-ref"}, "Your sessions")
	)
}


function sessionPlayer(sid, player, authenticated) {
	const playerView = div({class:`session__player`, id:`player-${player.pid}`},
		a(`#` + `${PLAYERS_URL}/` + `${player.pid}`, null, player.name),
	)
	if (authenticated) {
		playerView.appendChild(
			button({
					class: "icon__button",
					id: "delete-player" + player.pid,
					onclick: (event) => { handleConfirmation(event, event.currentTarget.id, function() { removePlayerFromSession(event, sid, player.pid) }) }
				},
				i({class: "fas fa-solid fa-ban fa-2x red"}),
			),
		)
	}
	return playerView
}


export {playerDetails, sessionPlayer}