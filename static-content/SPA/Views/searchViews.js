import {button, div, form} from "../WebDSL/web_dsl.js";
import {
	dateTimeInput,
	errorMessage,
	formInputField,
	formInputWithSearchResults,
	genresInput,
	sessionStateInput
} from "./models.js";

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
			genresInput(),
			errorMessage("err_message-game", "Game name must be at least 3 characters long"),
			button({type: "submit"}, "Search")
		)
	);
}

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

