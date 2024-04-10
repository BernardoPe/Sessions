import {br, button, div, fieldset, form, input, label, legend, p} from "../WebDSL/web_dsl.js";

function gameSearchView() {
	return div({class: "form__group"},
		form({id: "gameSearchForm", method: "GET"},
			div({class: "form__input"},
				input({
					id: "developer",
					class: "form__field",
					type: "text",
					placeholder: "Enter the name of developer",
					name: "developer",
					required: "true"
				}),
				label("developer", {class:"form__label", required:true}, "Developer name"),
			),

			fieldset(null,
				legend(null, "Select Genres:"),
				input({id: "RPG", type: "checkbox", name: "genre", value: "RPG"}),
				label("RPG", null, "RPG"),
				br(null),
				input({id: "Adventure", type: "checkbox", name: "genre", value: "Adventure"}),
				label("Adventure", null, "Adventure"),
				br(null),
				input({id: "Shooter", type: "checkbox", name: "genre", value: "Shooter"}),
				label("Shooter", null, "Shooter"),
				br(null),
				input({id: "TurnBased", type: "checkbox", name: "genre", value: "Turn-Based"}),
				label("TurnBased", null, "TurnBased"),
				br(null),
				input({id: "Action", type: "checkbox", name: "genre", value: "Action"}),
				label("Action", null, "Action"),
				br(null),
			),
			div({class: "error-message-container"},
				p({id: "err_message", class: "error_message"}, "Please select at least one genre")
			),
			button({type: "submit"}, "Search")
		)
	);
}

function sessionSearchView() {

	return div({class: "form__group"},
		form({id: "sessionSearchForm", method: "GET"},

			div({class: "form__input"},
				input({id: "game", class:"form__field", type: "number", placeholder: "Enter game id"}),
				label("game", {class:"form__label", required:true}, "Game identifier"),
			),

			div({class: "form__input"},
				input({id: "player", class:"form__field", type: "number", placeholder: "Enter player id"}),
				label("player", {class:"form__label", required:true}, "Player identifier"),
			),

			fieldset(null,
				legend(null, "Session state:"),
				input({type: "radio", id: "state1", name: "state", value: "OPEN"}),
				label("state1", null, "Open"),
				br(null),
				input({type: "radio", id: "state2", name: "state", value: "CLOSE"}),
				label("state2", null, "Closed"),
				br(null),
			),
			div({class: "form__input"},
				input({id: "date", type: "datetime-local", class:"form__field datepicker", placeholder: "yyyy-mm-dd", name: "date", value: ""}),
				label("date", {class:"form__label"}, "Date"),
			),
			button({type: "submit"}, "Search"),
		)
	);
}

export { gameSearchView, sessionSearchView };

