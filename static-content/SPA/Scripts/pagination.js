import {button, div} from "../WebDSL/web_dsl.js";

import {API_URL, GAMES_URL, SESSIONS_URL} from "../../index.js";
import {RESULTS_PER_PAGE} from "../handlers.js";

function handleGamePagination(queries, page, total) {
	return handlePagination(API_URL + GAMES_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + GAMES_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + GAMES_URL + `?page=${page}`;
		}
	}, total);
}

function handleSessionPagination(queries, page, total) {
	return handlePagination(API_URL + SESSIONS_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + SESSIONS_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + SESSIONS_URL + `?page=${page}`;
		}
	}, total);
}

function handlePagination(url, page, generateUrl, total) {
	const divElement = div({class: "pagination"});
	const skip = (page - 1) * RESULTS_PER_PAGE;

	if (parseInt(skip) > 0) {
		divElement.appendChild(
			createPaginationButton("Previous", parseInt(page) - 1, generateUrl)
		)
	}

	if (parseInt(skip) + RESULTS_PER_PAGE < total) {
		divElement.appendChild(
			createPaginationButton("Next", parseInt(page) + 1, generateUrl)
		)
	}

	return divElement;

}

function createPaginationButton(text, page, generateUrl) {
	const buttonElement = button({type: "button", class:"pagination-button"}, text);
	buttonElement.addEventListener('click', () => {
		window.location.href = generateUrl(page);
	});
	return buttonElement;
}


export {handleGamePagination, handleSessionPagination};