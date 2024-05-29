import {GAMES_URL, SESSIONS_URL} from "../../index.js";
import {RESULTS_PER_PAGE} from "../handlers.js";
import {createPaginationNav} from "../Views/Layouts/Nav/pagination.js";

/**
 * Handles pagination for the games page
 *
 * This function calls handlePagination, with a generated callback function that is used to generate the url for the pagination buttons,
 * supporting parameterized page numbers to create multiple references to other pages.
 *
 * @param queries - the queries to be used in the url
 * @param page - the current page
 * @param total - the total number of results matching the given queries
 * @returns {any}
 */
function handleGamePagination(queries, page, total) {
	return handlePagination( "#" +  GAMES_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + GAMES_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + GAMES_URL + `?page=${page}`;
		}
	}, total);
}

/**
 * Handles pagination for the sessions page
 *
 * This function calls handlePagination, with a generated callback function that is used to generate the url for the pagination buttons,
 * supporting parameterized page numbers to create multiple references to other pages.
 *
 * @param queries - the queries to be used in the url
 * @param page - the current page
 * @param total - the total number of results matching the given queries
 * @returns {any}
 */

function handleSessionPagination(queries, page, total) {
	return handlePagination( "#" + SESSIONS_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + SESSIONS_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + SESSIONS_URL + `?page=${page}`;
		}
	}, total);
}


/**
 * Handles the pagination for the given page.
 *
 * This function generates a set of pagination reference buttons that allow the user to navigate through the results.
 *
 * @param url - the url to be used for the pagination
 * @param page - the current page
 * @param generateUrl - the callback function to generate the url for the desired page
 * @param total - the total number of results matching the given queries
 * @returns {any}
 */

function handlePagination(url, page, generateUrl, total) {
	const skip = (page - 1) * RESULTS_PER_PAGE;
	const maxPage = Math.ceil(total / RESULTS_PER_PAGE);
	return createPaginationNav(url, skip, page, maxPage, generateUrl);
}


export {handleGamePagination, handleSessionPagination};