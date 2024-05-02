let timer;

window.handleSearch = handleSearch;
window.showSearchResults = showSearchResults;
window.resultsKeyPressHandler = resultsKeyHandler;
window.hideSearchResults = hideSearchResults;

/**
 * Handles API calls for search results and displays them in a container below the input field
 *
 * The search results are displayed as a list of clickable items, to be selected by the user.
 *
 * This function is called every time the user types in the input field, only makes the API call
 * after the user has stopped typing for 100ms. The purpose of this is to avoid api calls for every
 * key press.
 *
 * @param event
 * @param id
 * @param searchType
 */
function handleSearch(event, id, searchType) {
    clearTimeout(timer);
    const searchResults = document.getElementById(`search_results_${id}`);
    searchResults.innerHTML = '';
    timer = setTimeout(() => {
        const search = event.target.value;
        if (search.length >= 3) {
            fetch(`/` + searchType + `?name=${search}&limit=20`)
                .then(response => response.status === 200 ? response.json() : Promise.reject(response))
                .then(data => {
                    searchResults.style.display = 'block';
                    searchResults.innerHTML = '';
                    data[searchType].forEach(elem => {
                        const li = document.createElement('li');
                        li.className = 'search_result';
                        li.textContent = elem.name;
                        li.onclick = () => {
                            document.getElementById(id).value = elem.name;
                            document.getElementById(`search_results_${id}`).style.display = 'none';
                        };
                        searchResults.appendChild(li);
                    });
                })
                .catch(res => {
                })
        }
    }, 100);
}

/**
 * Shows the search results container for the given input field
 * @param id - the id of the input field
 */
function showSearchResults(id) {
    const searchResults = document.getElementById(`search_results_${id}`);
    if (searchResults.children.length > 1 || searchResults.children.item(0).tagName === 'LI') {
        document.getElementById(`search_results_${id}`).style.display = 'block';
    }
}

/**
 * Hides the search results container for the given input field
 * @param id - the id of the input field
 */
function hideSearchResults(id) {
    const searchResults = document.getElementById(`search_results_${id}`);
    if (searchResults) {
        searchResults.style.display = 'none';
    }
}

/**
 * Handles key events for search results
 *
 * Allows the user to navigate the search results using the arrow keys and select a result
 * using the enter key or by clicking on the result
 *
 * @param event
 * @param id
 */
function resultsKeyHandler(event, id) {
    const searchResults = document.getElementById(`search_results_${id}`);
    const searchResultsList = searchResults.children;
    const searchResultsLength = searchResultsList.length;
    const searchResultsVisible = searchResults.style.display === 'block';
    if (searchResultsVisible && searchResultsLength > 0) {
        const searchResultsSelected = searchResults.querySelector('.selected');
        switch (event.key) {
            case 'ArrowDown':
                event.preventDefault();
                if (searchResultsSelected) {
                    searchResults.scrollTop = searchResultsSelected.offsetTop;
                    searchResultsSelected.classList.remove('selected');
                    if (searchResultsSelected.nextElementSibling)
                        searchResultsSelected.nextElementSibling.classList.add('selected');
                    else if (searchResultsSelected === searchResultsList.item(searchResultsLength - 1)) {
                        searchResults.scrollTop = 0;
                        searchResultsList.item(0).classList.add('selected');
                    } else {
                        searchResultsList.item(0).classList.add('selected');
                    }
                } else {
                    searchResultsList.item(0).classList.add('selected');
                }
                break;
            case 'ArrowUp':
                event.preventDefault();
                if (searchResultsSelected) {
                    searchResults.scrollTop = searchResultsSelected.offsetTop - searchResults.clientHeight + searchResultsSelected.clientHeight
                    searchResultsSelected.classList.remove('selected');
                    if (searchResultsSelected.previousElementSibling) {
                        searchResultsSelected.previousElementSibling.classList.add('selected');
                    } else if (searchResultsSelected === searchResultsList.item(0)) {
                        searchResults.scrollTop = searchResults.scrollHeight;
                        searchResultsList.item(searchResultsLength - 1).classList.add('selected');
                    } else {
                        searchResultsList.item(searchResultsLength - 1).classList.add('selected');
                    }
                } else {
                    searchResults.scrollTop = searchResults.scrollHeight;
                    searchResultsList.item(searchResultsLength - 1).classList.add('selected');
                }
                break;
            case 'Enter':
                event.preventDefault();
                if (searchResultsSelected) {
                    document.getElementById(id).value = searchResultsSelected.textContent;
                    searchResults.style.display = 'none';
                }
                break;
            default:
                break;
        }
    }
}

export {
    handleSearch,
    showSearchResults,
    resultsKeyHandler,
    hideSearchResults
}