import {a, div, h2, li, ol, p} from "../WebDSL/web_dsl.js";

export function homeView (user) {
	const welcome = user ? `Welcome to Sessions, ${user.name}` : "Welcome to Sessions";

	const homeDiv = div(null,
		h2({class:'title'}, welcome),
	);

	const items = ol(null,
			li(null, a("#games/search", null, "Game Search")),
			li(null, a("#sessions/search", null, "Session Search")),
	)

	if (user) {
		items.appendChild(
			li(null, a("#games/create", null, "Create Game")),
		);
		items.appendChild(
			li(null, a("#sessions/create", null, "Create Session")),
		)
		homeDiv.appendChild(
			h2({class:'subtitle'},
				a(`#players/${user.pid}`, null, "Your profile"),
			),
		)
	}

	homeDiv.appendChild(items);

	return homeDiv;
}