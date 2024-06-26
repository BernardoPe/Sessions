import {a, div, h2, i, li, ol, p} from "../../../WebDSL/web_dsl.js";

/**
 * Home view for the Sessions app - displays a welcome message and links to other views
 *
 * If a user is logged in, it also displays a link to the user's profile and links to create a game and session
 *
 * @param user - the user currently logged in, null if no user is logged in
 */
function homeView (user) {
	const welcome = user ? `Welcome to Sessions, ${user.name}` : "Welcome to Sessions";

	const homeDiv = div({class:'fade-up'},
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
				a(`#players/${user.pid}`, {class:'icon-link'},
					i({class:'fas fa-user'}),
					p(null, "Profile")
				),
			),
		)
	}

	homeDiv.appendChild(items);

	return homeDiv;
}

export {homeView}