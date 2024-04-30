import {button, div, form} from "../WebDSL/web_dsl.js";
import {
	dateTimeInput,
	errorMessage,
	formInputField,
	formInputWithSearchResults,
	genresInput,
	sessionStateInput
} from "./models.js";

/**
 * View for the game search form
 *
 * For details about the game search operation, see [Game Search]{@link submitFormGameSearch}
 */
function gameSearchView() {
	return div({class: "form__group"},
		form({onsubmit: "submitFormGameSearch(event)"},
			formInputField(
				"developer",
				"developer",
				"text",
				"Developer name"
			),
			errorMessage("err_message-developer", "Developer name must be at least 3 characters long"),
			formInputWithSearchResults(
				"game",
				"games",
				"text",
				"Game name"
			),
			errorMessage("err_message-game", "Game name must be at least 3 characters long"),
			genresInput(),
			button({type: "submit"}, "Search")
		)
	);
}


/**
 * View for the session search form
 *
 * For details about the session search operation, see [Session Search]{@link submitFormSessionSearch}
 */
function sessionSearchView() {
	return div({class: "form__group"},
		form({onsubmit: "submitFormSessionSearch(event)"},
			formInputWithSearchResults(
				"game",
				"games",
				"text",
				"Game name"
			),
			errorMessage("err_message-game", "Game name must be at least 3 characters long"),
			formInputWithSearchResults(
				"player",
				"players",
				"text",
				"Player name"
			),
			errorMessage("err_message-player", "Player name must be at least 3 characters long"),
			sessionStateInput(),
			dateTimeInput(),
			button({type: "submit"}, "Search"),
		)
	);
}

export { gameSearchView, sessionSearchView };

