import {a, div, h2, li, ol} from "../WebDSL/web_dsl.js";

export function homeView () {
	return div(null,
		h2({class:'title'}, "Welcome to Sessions "),
		ol(null,
			li(null, a("#games/search", null, "Game Search")),
			li(null, a("#sessions/search", null, "Session Search")),
			li(null, a("#players/1", null, "Player Details")) // hardcoded player id
		),
	);
}