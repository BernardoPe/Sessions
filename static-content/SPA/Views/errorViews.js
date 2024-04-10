import {button, div, p} from "../WebDSL/web_dsl.js";

function notFoundView() {
	return div({class:"error-container"},
		p({class:"not-found-message"},"No results were found"),
		button({type: "button"}, "Return"),
	)
}
function genericErrorView() {
	return div({class:"error-container"},
		p({class:"error-message"},"An error occurred"),
		button({type: "button"}, "Return"),
	)
}

export {notFoundView, genericErrorView}