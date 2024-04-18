import {a, div, fieldset, h1, legend, p} from "../WebDSL/web_dsl.js";

function gameDetailsView(game) {
	return div(null,
		div({class:"game-container"},
			h1({class:"game__title"}, game.name),
			p({class:"game__developer"}, "By " + game.developer),
			p({class:"game__genres"}, "Genres: " + game.genres.join(', ')),
		),
		a(`#sessions/searchResults?gid=${game.gid}`, {class:"search-ref"}, "Sessions with this game")
	)
}

function sessionDetailsView(session) {
	return div({class:"session-container"},
		p({class:"session__game"},
			a(`#games/${session.gameSession.gid}`, null, session.gameSession.name)
		),
		p({class:"session__date"}, session.date),
		fieldset({class:"session__players"},
			legend(null, "Players " + session.playersSession.length + "/" + session.capacity),
			...session.playersSession.map(
				player =>
					div({class:"session__player"},
						a(`#players/${player.pid}`, null, player.name)
					)
			)
		),
	)
}

function playerDetailsView(player) {
	return div(null,
		div({class: "player-container"},
			p({class: "player__name"}, player.name),
			p({class: "player__email"}, "Contact info: " + player.email)
		),
		a(`#sessions/searchResults?pid=${player.pid}`, {class: "search-ref"}, "Your sessions")
	)
}

export {gameDetailsView, sessionDetailsView, playerDetailsView}