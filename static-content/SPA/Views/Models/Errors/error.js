
import {div, p} from "../../../WebDSL/web_dsl.js";


/**
 * Returns an HTML structure for an error message
 */
function errorMessage(id, msg) {
	return div({id: id, class: "error-message-container fade-up", onclick: "this.style.display = 'none'"},
		p({class: "error_message"}, msg)
	)
}

export {errorMessage}