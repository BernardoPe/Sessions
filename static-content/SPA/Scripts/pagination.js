import {button, div} from "../WebDSL/web_dsl.js";

import {API_URL} from "../handlers.js";
function handleGamePagination(queries, limit, skip, total) {
	return handlePagination(API_URL + `games?${queries}`, limit, skip, (limit, skip) => {
		if (queries.toString().length > 0) {
			return `#games/searchResults?${queries}&limit=${limit}&skip=${skip}`;
		} else {
			return `#games/searchResults?limit=${limit}&skip=${skip}`;
		}
	}, total);
}

function handleSessionPagination(queries, limit, skip, total) {
	return handlePagination(API_URL + `sessions?${queries}`, limit, skip, (limit, skip) => {
		if (queries.toString().length > 0) {
			return `#sessions/searchResults?${queries}&limit=${limit}&skip=${skip}`;
		} else {
			return `#sessions/searchResults?limit=${limit}&skip=${skip}`;
		}
	}, total);
}

function handlePagination(url, limit, skip, generateUrl, total) {

	const divElement = div({class: "pagination"});

	if (parseInt(skip) > 0) {
		let resSkip = parseInt(skip) - parseInt(limit);
		if (resSkip < 0) {
			resSkip = 0;
		}
		divElement.appendChild(
			createPaginationButton("Previous", limit, resSkip, generateUrl)
		)
	}

	if (parseInt(skip) + parseInt(limit) < total) {
		divElement.appendChild(
			createPaginationButton("Next", limit, parseInt(skip) + parseInt(limit), generateUrl)
		)
	}

	return divElement;

}

function createPaginationButton(text, limit, skip, generateUrl, total) {
	const buttonElement = button({type: "button", class:"pagination-button"}, text);
	buttonElement.addEventListener('click', () => {
		window.location.href = generateUrl(limit, skip);
	});
	return buttonElement;
}


export {handleGamePagination, handleSessionPagination};