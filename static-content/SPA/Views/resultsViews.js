import {a, br, div, p} from "../WebDSL/web_dsl.js";

function sessionSearchResultsView(sessions) {
	return div({class: "search-results-container"},
		a("#sessions/search", {class: "session__reference"}, "Search for more sessions"),
		...sessions.map(s =>
			div({class: "session-container"},
				p({class: "session__game"},
					a(`#games/${s.gameSession.gid}`, null, s.gameSession.name)
				),
				p({class: "session__date"}, s.date),
				a(`#sessions/${s.sid}`, {class: "session__reference"}, "Get more details"),
				br(null)
			)
		),
	);
}

function gameSearchResultsView(games) {
	return div({class: "search-results-container"},
		...games.map(g =>
			div({class: "game-container"},
				p({class: "game__title"},
					a(`#games/${g.gid}`, null, g.name)
				),
				p({class: "game__developer"}, g.developer),
				br(null)
			)
		),
	);
}

export {sessionSearchResultsView, gameSearchResultsView};