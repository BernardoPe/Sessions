
import {a, button, div, p} from "../../../WebDSL/web_dsl.js";
import {PLAYERS_URL, SESSIONS_URL} from "../../../../index.js";

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
	const playerView = div({class:`session__player_${player.pid}`},
		a(`#` + `${PLAYERS_URL}/` + `${player.pid}`, null, player.name),
	)
	if (authenticated) {
		playerView.appendChild(
			button({
				class: "session__delete__button",
				id: "remove_player",
				type: "button",
				onclick: `removePlayerFromSession(event,${sid}, ${player.pid})`
			}, "Remove Player"),
		)
	}
	return playerView
}


export {playerDetails, sessionPlayer}