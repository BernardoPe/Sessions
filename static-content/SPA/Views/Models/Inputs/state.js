import {br, fieldset, input, label, legend} from "../../../WebDSL/web_dsl.js";

/**
 * Returns an HTML structure for a session state input field
 */
function sessionStateInput() {
	return fieldset(null,
		legend(null, "Session state:"),
		input({type: "radio", id: "state1", name: "state", value: "OPEN"}),
		label("state1", null, "Open"),
		br(null),
		input({type: "radio", id: "state2", name: "state", value: "CLOSE"}),
		label("state2", null, "Closed"),
		br(null),
	)
}


export {sessionStateInput}