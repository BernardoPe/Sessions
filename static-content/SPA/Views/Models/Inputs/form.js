import {div, input, label, ul} from "../../../WebDSL/web_dsl.js";

/**
 * Returns an HTML structure for a form input field
 * @param id - id of the input field
 * @param name - name of the input field
 * @param type - type of the input field
 * @param title - title of the input field
 */
function formInputField(id, name, type, title) {
	return div({class: "form__input"},
		input({
			id: id,
			class: "form__field",
			type: type,
			placeholder: "",
			name: name,
			autocomplete: "off"
		}),
		label(id, {class: "form__label"}, title),
	)
}



/**
 * Returns an HTML structure for a form input field with search results
 * @param id - id of the input field
 *
 * @param searchType - type of the search. This is used to determine the name search endpoint to call from the API.
 *
 * @param fieldType - type of the input field
 *
 * @param title - title of the input field
 */

function formInputWithSearchResults(id, searchType, fieldType, title) {
	return div({class: "form__input"},
		input({
			id: id,
			class: "form__field",
			type: fieldType,
			placeholder: "",
			name: searchType,
			autocomplete: "off",
			oninput: "handleSearch(event, id, name)",
			oncut: "handleSearch(event, id, name)",
			onpaste: "handleSearch(event, id, name)",
			onfocus: "showSearchResults(id); handleSearch(event, id, name)",
			onblur: "setTimeout(() => hideSearchResults(id), 100)", // delay to allow click on search result
			onkeydown: "resultsKeyPressHandler(event, id)",
		}),
		label(id, {class: "form__label"}, title),
		ul({class: "search_results", id: "search_results_" + id }, div({class: "search-results"})),
	)
}

export {formInputField, formInputWithSearchResults}