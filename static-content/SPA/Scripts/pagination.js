import {button, div} from "../WebDSL/web_dsl.js";

import {API_URL} from "../handlers.js";
function handleGamePagination(developer, genres, limit, skip) {
	return handlePagination(API_URL + `games?developer=${developer}&genres=${genres}`, limit, skip, (limit, skip) => {
		return `#games/searchResults?developer=${developer}&genres=${genres}&limit=${limit}&skip=${skip}`;
	});
}

function handleSessionPagination(queries, limit, skip) {
	return handlePagination(API_URL + `sessions?${queries}`, limit, skip, (limit, skip) => {
		if (queries.toString().length > 0) {
			return `#sessions/searchResults?${queries}&limit=${limit}&skip=${skip}`;
		} else {
			return `#sessions/searchResults?limit=${limit}&skip=${skip}`;
		}
	});
}

function handlePagination(url, limit, skip, generateUrl) {

	const divElement = div({class: "pagination"});

	if (skip >= limit) {
		divElement.appendChild(
			createPaginationButton("Previous", limit, parseInt(skip) - parseInt(limit), generateUrl)
		)
	}

	let queryStr = '';
	const urlParts = url.split('?');

	if (urlParts.length > 1 && urlParts[1].trim() !== '') {
		queryStr = url + `&limit=${limit}&skip=${parseInt(skip) + parseInt(limit)}`;
	} else {
		queryStr = url + `limit=${limit}&skip=${parseInt(skip) + parseInt(limit)}`;
	}

	fetch(queryStr)
		.then(res => {
			if (res.ok) {
				divElement.appendChild(
					createPaginationButton("Next", limit, parseInt(skip) + parseInt(limit), generateUrl)
				)
			}
		})

	return divElement;

}

function createPaginationButton(text, limit, skip, generateUrl) {
	const buttonElement = button({type: "button", class:"pagination-button"}, text);
	buttonElement.addEventListener('click', () => {
		window.location.href = generateUrl(limit, skip);
	});
	return buttonElement;
}


export {handleGamePagination, handleSessionPagination};