import {button, div, p, path, svg, ul} from "../../../WebDSL/web_dsl.js";
import {RESULTS_PER_PAGE} from "../../../handlers.js";


function createPaginationNav(url, skip, page, maxPage, generateUrl) {

	const total = parseInt(maxPage) * RESULTS_PER_PAGE;

	const info = div({class: "pagination-info"},
		p(null,
			`Showing ${skip + 1}-${Math.min(skip + RESULTS_PER_PAGE, total)} of ${total} results`
		)
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

	return info
}


/**
 * Generates the numbered pagination items for the given page.
 *
 * This function generates five numbered pagination items, with the current page in the middle.
 *
 * If the current page is at least two pages away from the first and last pages, and the total number of pages is greater than 5,
 * the pagination items will include a spacer to indicate that there are more pages to be shown, and a reference to the first and last pages.
 *
 * @param url - the url to be used for the pagination
 * @param page - the current page
 * @param maxPage - the maximum number of pages
 * @param generateUrl - the callback function to generate the url for the desired page
 */
function paginationItems(url, page, maxPage, generateUrl) {
	const items = [];
	const slices = 5;
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

/**
 * Creates a numbered pagination item for the given page.
 * @param page - the page number
 * @param currentPage - the current page. Used to determine if the item is active or not.
 * @param generateUrl - the callback function to generate the url for the desired page
 * @returns {any}
 */

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

/**
 * Creates an arrow pagination button for the given page.
 *
 * Used for the next and previous page references.
 *
 * @param page - the page number
 * @param generateUrl - the callback function to generate the url for the desired page
 * @param next - whether the button is for the next page or the previous page
 */
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

export {paginationItems, createPaginationItem, createPaginationButton, createPaginationNav};