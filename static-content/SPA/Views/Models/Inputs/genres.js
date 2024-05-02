/**
 * Returns an HTML structure for a genre input field
 */
import {br, fieldset, input, label, legend} from "../../../WebDSL/web_dsl.js";


function genresInput() {
	return fieldset(null,
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
	)
}

export {genresInput}