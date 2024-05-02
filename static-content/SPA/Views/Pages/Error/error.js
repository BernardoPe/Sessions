import {button, div, p} from "../../../WebDSL/web_dsl.js";

/**
 * A view that is shown when no results are found for a search
 */
function notFoundView() {
	return div({class:"error-container"},
		p({class:"not-found-message"},"No results were found"),
		button({type: "button", onclick:"window.history.back()"}, "Return"),
	)
}

/**
 * A generic error view that is used when an unexpected error occurs
 *
 * Ideally, this view should never be shown to the user, but it is here as a fallback to
 * avoid the user not being aware of an error happening.
 *
 */
function genericErrorView() {
	return div({class:"error-container"},
		p({class:"error-message"},"An error occurred"),
		button({type: "button", onclick:"window.history.back()"}, "Return"),
	)
}

export {notFoundView, genericErrorView}