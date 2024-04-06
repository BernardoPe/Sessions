/*
This example creates the students views using directly the DOM Api
But you can create the views in a different way, for example, for the student details you can:
    createElement("ul",
        createElement("li", "Name : " + student.name),
        createElement("li", "Number : " + student.number)
    )
or
    ul(
        li("Name : " + student.name),
        li("Number : " + student.name)
    )
Note: You have to use the DOM Api, but not directly
*/

const API_URL = 'http://localhost:8080/';

const RESULTS_PER_PAGE = 5;

import {a, br, button, div, fieldset, form, h1, h2, input, label, legend, li, ol, p, ul} from './WebDSL/web_dsl.js';

function getHome(mainContent, req) {
    const h2Element =
        h2({class:'title'}, "Welcome to Sessions ");
    const olElement =
        ol(null,
            li(null, a("#games/search", null, "Game Search")),
            li(null, a("#sessions/search", null, "Session Search")),
            li(null, a("#players/1", null, "Player Details")) // hardcoded player id
        );
    mainContent.replaceChildren(h2Element, olElement);
}

function getGameSearch(mainContent, req) {
    const forms =
        div({class: "form__group"},
            form({id: "gameSearchForm", method: "GET"},
                div({class: "form__input"},
                    input({id: "developer",class:"form__field", type: "text", placeholder: "Enter the name of developer", name: "developer"}),
                    label("developer", {class:"form__label", required:true}, "Developer name"),
                ),
                fieldset(null,
                    legend(null, "Select Genres:"),
                    input({id: "RPG", type: "checkbox", name: "genre", value: "RPG"}),
                    label("RPG", null, "RPG"),
                    br(null),
                    input({id: "Adventure", type: "checkbox", name: "genre", value: "Adventure"}),
                    label("Adventure", null, "Adventure"),
                    br(null),
                    input({id: "Shooter", type: "checkbox", name: "genre", value: "Shooter"}),
                    label("Shooter", null, "Shooter"),
                    br(null),
                    input({id: "TurnBased", type: "checkbox", name: "genre", value: "Turn-Based"}),
                    label("TurnBased", null, "TurnBased"),
                    br(null),
                    input({id: "Action", type: "checkbox", name: "genre", value: "Action"}),
                    label("Action", null, "Action"),
                    br(null),
                ),
                button({type: "submit"}, "Search")
            )
        );
    mainContent.replaceChildren(forms);
    document.getElementById('gameSearchForm').addEventListener('submit', submitFormGameSearch);
}

function getGameSearchResults(mainContent, req) {
    const developer = req.query.developer;
    const genres = req.query.genres;
    const limit = req.query.limit ? req.query.limit : RESULTS_PER_PAGE
    const skip = req.query.skip ? req.query.skip : 0;
    fetchWithHandling(API_URL + 'games?developer=' + developer + '&genres=' + genres + '&limit=' + limit + '&skip=' + skip, mainContent, (games) => {
        const divElement = div({class: "search-results-container"},
            ...games.map(g =>
                div({class: "game-container"},
                    p({class: "game__title"},
                        a(`#games/${g.gid}`, null, g.name)
                    ),
                    p({class: "game__developer"}, g.developer),
                    br(null)
                )
            ),
            handleGamePagination(developer, genres, limit, skip)
        );
        mainContent.replaceChildren(divElement);
    })
}

function getSessionSearch(mainContent, req) {
    const forms =
        div({class: "form__group"},
            form({id: "sessionSearchForm", method: "GET"},

                div({class: "form__input"},
                    input({id: "game", class:"form__field", type: "number", placeholder: "Enter game id"}),
                    label("game", {class:"form__label", required:true}, "Game identifier"),
                ),

                div({class: "form__input"},
                    input({id: "player", class:"form__field", type: "number", placeholder: "Enter player id"}),
                    label("player", {class:"form__label", required:true}, "Player identifier"),
                ),

                fieldset(null,
                    legend(null, "Session state:"),
                    input({type: "radio", id: "state1", name: "state", value: "OPEN"}),
                    label("state1", null, "Open"),
                    br(null),
                    input({type: "radio", id: "state2", name: "state", value: "CLOSE"}),
                    label("state2", null, "Closed"),
                    br(null),
                ),
                div({class: "form__input"},
                    input({id: "date", type: "datetime-local", class:"form__field datepicker", placeholder: "yyyy-mm-dd", name: "date", value: ""}),
                    label("date", {class:"form__label"}, "Date"),
                ),
                button({type: "submit"}, "Search"),
            )
        );
    mainContent.replaceChildren(forms);
    document.getElementById('sessionSearchForm').addEventListener('submit', submitFormSessionSearch);
}

function getSessionSearchResults(mainContent, req) {

    const queries = new URLSearchParams();

    const gameId = req.query.gid ? queries.append('gid', req.query.gid) : null;
    const playerId = req.query.pid ? queries.append('pid', req.query.pid) : null;
    const state = req.query.state ? queries.append('state', req.query.state) : null;
    const date = req.query.date ? queries.append('date', req.query.date) : null;
    const limit = req.query.limit ? req.query.limit : RESULTS_PER_PAGE;
    const skip = req.query.skip ? req.query.skip : 0;

    let queryStr = queries.toString();

    if (queryStr.length === 0) {
        queryStr = `limit=${limit}&skip=${skip}`;
    } else {
        queryStr += `&limit=${limit}&skip=${skip}`;
    }

    fetchWithHandling(API_URL + `sessions?${queryStr}`, mainContent, (sessions) => {
        const divElement = div({class: "search-results-container"},
            ...sessions.map(s =>
                div({class: "session-container"},
                    p({class: "session__game"},
                        a(`#games/${s.gameSession.gid}`, null, s.gameSession.name)
                    ),
                    p({class: "session__date"}, s.date),
                    a(`#sessions/${s.sid}`, {class: "session__reference"}, "Get more details"),
                    br(null)
                )
            ),
            handleSessionPagination(queries, limit, skip)
        );
        mainContent.replaceChildren(divElement);
    })
}

function getGameDetails(mainContent, req) {
    const gameId = req.params.gid
    fetchWithHandling(API_URL + `games/${gameId}`, mainContent, (game) => {
        const gameView = div({class:"game-container"},
            h1({class:"game__title"}, game.name),
            p({class:"game__developer"}, "By " + game.developer),
            p({class:"game__genres"}, "Genres: " + game.genres.join(', ')),
            p({class:"game__identifier"}, "Game ID: " + game.gid)
        )
        const anchor = a(`#sessions/searchResults?gid=${game.gid}`, {class:"search-ref"}, "Sessions with this game")
        mainContent.replaceChildren(gameView, anchor)
    })
}

function getSessionDetails(mainContent, req) {
    const sessionId = req.params.sid
    fetchWithHandling(API_URL + `sessions/${sessionId}`, mainContent, (session) => {
        const sessionView = div({class:"session-container"},
            p({class:"session__game"},
                a(`#games/${session.gameSession.gid}`, null, session.gameSession.name)
            ),
            p({class:"session__date"}, session.date),
            p({class:"session__identifier"}, `Session ID: ${session.sid}`),
            fieldset({class:"session__players"},
                legend(null, "Players " + session.playersSession.length + "/" + session.capacity),
                ...session.playersSession.map(
                    player =>
                        p({class:"session__player"},
                            a(`#players/${player.pid}`, null, player.name)
                        )
                )
            ),
        )
        mainContent.replaceChildren(sessionView)
    })
}

function getPlayerDetails(mainContent, req) {
    const playerId = req.params.pid
    fetchWithHandling(API_URL + `players/${playerId}`, mainContent, (player) => {
        const playerView = div({class: "player-container"},
            p({class: "player__name"}, player.name),
            p({class: "player__identifier"}, "Player ID : " + player.pid),
            p({class: "player__email"}, "Contact info: " + player.email)
        )
        const anchor = a(`#sessions/searchResults?pid=${player.pid}`, {class: "search-ref"}, "Sessions with this player")
        mainContent.replaceChildren(playerView, anchor)
    })
}


function submitFormGameSearch(event) {
    event.preventDefault();
    const developer = document.getElementById('developer').value;
    const checkedCheckboxes = document.querySelectorAll('input[name="genre"]:checked');
    const genres = Array.from(checkedCheckboxes).map(checkbox => checkbox.value).join(',');
    window.location.href = `#games/searchResults?developer=${developer}&genres=${genres}`;
}

function submitFormSessionSearch(event) {
    event.preventDefault();
    const gameId = document.getElementById('game').value;
    const playerId = document.getElementById('player').value;
    const stateElement = document.querySelector('input[name="state"]:checked');
    const state = stateElement ? stateElement.value : null;
    const date = document.getElementById('date').value;

    const queries = new URLSearchParams();

    if (gameId) {
        queries.append('gid', gameId);
    }

    if (playerId) {
        queries.append('pid', playerId);
    }

    if (state) {
        queries.append('state', state);
    }

    if (date) {
        queries.append('date', date);
    }

    if (queries.toString().length > 0)
        window.location.href = `#sessions/searchResults?${queries}`;
    else
        window.location.href = `#sessions/searchResults`;
}

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

function fetchWithHandling(url, mainContent, onSuccess) {
    fetch(url)
        .then(res => {
            res.ok ? res.json().then(res => onSuccess(res)) : handleErrors(res, mainContent)
        })
}

function handleErrors(res, mainContent) {
    if (res.status === 404) {
        const returnButton = button({type: "button"}, "Return");
        returnButton.addEventListener('click', () => {
            history.back()
        });
        mainContent.replaceChildren(
            div({class:"error-container"},
                p({class:"not-found-message"},"No results were found"),
                returnButton
            )
        )
    } else {
        mainContent.replaceChildren(h1(null, "An error occurred"))
    }
}

export default {
    getHome,
    getGameSearch,
    getSessionSearch,
    getGameSearchResults,
    getSessionSearchResults,
    getGameDetails,
    getSessionDetails,
    getPlayerDetails
}