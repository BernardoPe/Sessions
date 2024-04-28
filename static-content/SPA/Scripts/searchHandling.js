let timer;

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

function showSearchResults(id) {
    const searchResults = document.getElementById(`search_results_${id}`);
    if (searchResults.children.length > 1 || searchResults.children.item(0).tagName === 'LI') {
        document.getElementById(`search_results_${id}`).style.display = 'block';
    }
}

function hideSearchResults(id) {
    const searchResults = document.getElementById(`search_results_${id}`);
    if (searchResults) {
        searchResults.style.display = 'none';
    }
}

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