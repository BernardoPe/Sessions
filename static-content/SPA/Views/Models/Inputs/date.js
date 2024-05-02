import {div, input, label} from "../../../WebDSL/web_dsl.js";

/**
 * Returns an HTML structure for a date time input field
 */
function dateTimeInput() {
	return div({class: "form__input"},
		input({id: "date", type: "datetime-local", class:"form__field datepicker", placeholder: "yyyy-mm-dd", name: "date", value: ""}),
		label("date", {class:"form__label"}, "Date"),
	)
}


export {dateTimeInput}