import {button, div, li, p, path, svg, ul} from "../WebDSL/web_dsl.js";

import {API_URL, GAMES_URL, SESSIONS_URL} from "../../index.js";
import {RESULTS_PER_PAGE} from "../handlers.js";

function handleGamePagination(queries, page, total) {
	return handlePagination(API_URL + "#" +  GAMES_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + GAMES_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + GAMES_URL + `?page=${page}`;
		}
	}, total);
}

function handleSessionPagination(queries, page, total) {
	return handlePagination(API_URL + "#" + SESSIONS_URL + `?${queries}`, page, (page) => {
		if (queries.toString().length > 0) {
			return `#` + SESSIONS_URL + `?${queries}&page=${page}`;
		} else {
			return `#` + SESSIONS_URL + `?page=${page}`;
		}
	}, total);
}

function handlePagination(url, page, generateUrl, total) {
	const skip = (page - 1) * RESULTS_PER_PAGE;
	const maxPage = Math.ceil(total / RESULTS_PER_PAGE);

    const info = div({class: "pagination-info"},
	    `Showing ${skip + 1}-${Math.min(skip + RESULTS_PER_PAGE, total)} of ${total} results`
    );

	const paginationNav = div({class: "pagination-nav"});

	if (parseInt(skip) > 0) {
		paginationNav.appendChild(
			createPaginationButton(parseInt(page) - 1, generateUrl, false)
		)
	}

	const list = ul({class: "pagination-list"})

	paginationItems(url, page, maxPage, generateUrl).forEach(item => {
		list.appendChild(item);
	})

	paginationNav.appendChild(list)

	if (parseInt(skip) + RESULTS_PER_PAGE < total) {
		paginationNav.appendChild(
			createPaginationButton(parseInt(page) + 1, generateUrl, true)
		)
	}

	info.appendChild(paginationNav);

	return info;

}

function paginationItems(url, page, maxPage, generateUrl) {
	const slices = 5;
	const items = [];
	const start = Math.max(1, page - Math.floor(slices / 2));
	const end = Math.min(maxPage, start + slices - 1);

	if (start > 1) {
		items.push(createPaginationItem(1, page, generateUrl));
	}

	if (start > 2) {
		items.push(p({class: "pagination-spacer"}, "..."));
	}

	for (let i = start; i <= end; i++) {
		items.push(createPaginationItem(i, page, generateUrl));
	}

	if (end < maxPage - 1) {
		items.push(p({class: "pagination-spacer"}, "..."));
	}

	if (end < maxPage) {
		items.push(createPaginationItem(maxPage, page, generateUrl));
	}

	return items;
}

function createPaginationItem(page, currentPage, generateUrl) {
	const divElement = div({class: "pagination-item"}, `${page}`);
	if (parseInt(page) === parseInt(currentPage)) {
		divElement.classList.add("active");
	} else {
		divElement.addEventListener('click', () => {
			window.location.href = generateUrl(page);
		});
	}
	return divElement;
}

function createPaginationButton(page, generateUrl, next) {
	const elemClass = next ? "pagination-button next" : "pagination-button prev";
	const buttonElement = button({class: elemClass},
		div({class: "pagination-ref"},
			svg({fill:"none", viewBox: "0 0 9 15"},
				path({d: "M8.339 7.37L1.619.604.04 2.195l5.142 5.18-5.142 5.172 1.58 1.593 6.72-6.77z", fill: "currentColor"}),
			),
		)
	);
	buttonElement.addEventListener('click', () => {
		window.location.href = generateUrl(page);
	});
	return buttonElement;
}

export {handleGamePagination, handleSessionPagination};