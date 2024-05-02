import {a, br, div, h1, p} from "../../../WebDSL/web_dsl.js";
import {SESSIONS_URL} from "../../../../index.js";

/**
 * Returns an HTML structure for a game's details
 * @param game - game object
 */

function gameDetails(game) {
	return div(null,
		div({class:"game-container fade-up"},
			h1({class:"game__title"}, game.name),
			p({class:"game__developer"}, "By " + game.developer),
			p({class:"game__genres"}, "Genres: " + game.genres.join(', ')),
		),
		a(`#` + `${SESSIONS_URL}` + `?gid=${game.gid}`, {class:"search-ref"}, "Sessions with this game")
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

export {gameDetails, gameSearchResult}