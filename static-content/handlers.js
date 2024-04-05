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

const RESULTS_PER_PAGE = 2;

import {a, br, button, div, fieldset, form, h1, h2, input, label, legend, li, ol, p, ul} from './WebDSL/web_dsl.js';

function getHome(mainContent, req) {
    const h2Element =
        h2(null, "Select the following options: ");
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
        div(null,
            form({id: "gameSearchForm", method: "GET"},
                label("developer", null, "Name of the developer"),
                input({id: "developer", type: "text", placeholder: "Enter the name of developer", name: "developer"}),
                fieldset(null,
                    legend(null, "Select Values:"),
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
    fetch(API_URL + 'games?developer=' + developer + '&genres=' + genres + '&limit=' + limit + '&skip=' + skip)
        .then(res => {
            if (!res.ok) {
                mainContent.replaceChildren(p(null, "Games not found"))
            } else {
                return res.json()
            }
        })
        .then(games => {
            const divElement = div(null,
                h1(null, "Game Search"),
                ...games.map(g =>
                    div(null,
                        p(null, "Name: " + g.name),
                        p(null, "Developer: " + g.developer),
                        a("#games/" + g.gid, null, "Get more details"),
                        br(null)
                    )
                ),
                handleGamePagination(developer, genres, limit, skip)
            );
            mainContent.replaceChildren(divElement); // Append the results below the form
        })
}

function getSessionSearch(mainContent, req) {
    const forms =
        div(null,
            form({id: "sessionSearchForm", method: "GET"},
                label("game", null, "Game identifier"),
                input({id: "game", type: "number", step: "1", name: "game ID"}),
                label("player", null, "PLayer identifier"),
                input({id: "player", type: "number", step: "1", name: "player ID"}),
                fieldset(null,
                    legend(null, "Select the state of the session:"),
                    input({type: "radio", id: "state1", name: "state", value: "OPEN"}),
                    label("state1", null, "Opened Sessions"),
                    br(null),
                    input({type: "radio", id: "state2", name: "state", value: "CLOSE"}),
                    label("state2", null, "Closed Sessions"),
                    br(null),
                ),
                label("date", null, "Date"),
                input({id: "date", type: "text", placeholder: "yyyy-mm-dd", name: "date", value: ""}),
                label("time", null, "Time"),
                input({id: "time", type: "text", placeholder: "hh:mm", name: "time", value: ""}),
                button({type: "submit"}, "Search")
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

    fetch(API_URL + `sessions?${queryStr} `)
        .then(res => {
            if (!res.ok) {
                mainContent.replaceChildren(p(null, "Sessions not found"))
            } else {
                return res.json()
            }
        })
        .then(sessions => {
            const divElement = div(null,
                h1(null, "Session Search"),
                ...sessions.map(s =>
                    p(null,
                        p(null, "Date: " + s.date),
                        p(null, "Game: " + s.gameSession.name),
                        a("#sessions/" + s.sid, null, "Get more details")
                    )
                ),
                handleSessionPagination(queries, limit, skip)
            );
            mainContent.replaceChildren(divElement); // Append the results below the form
        })
}

function getGameDetails(mainContent, req) {
    const gameId = req.params.gid
    fetch(API_URL + `games/${gameId}`)
        .then(res => {
            if (!res.ok) {
                mainContent.replaceChildren(p(null, "Game not found"))
            } else {
                return res.json()
            }
        })
        .then(game => {
            const h2Game = h2(null, "Game details")
            const list = ul(null,
                li(null, "ID : " + game.gid),
                li(null, "Name : " + game.name),
                li(null, "Developer : " + game.developer),
                li(null,
                    p(null, "Genres : "),
                    ul(null,
                        ...game.genres.map(genre => li(null, genre))
                    )
                )
            )
            const anchor = a(`#sessions/searchResults?gid=${game.gid}`, null, "Sessions with this Game")
            mainContent.replaceChildren(h2Game, list, anchor)
        })
}

function getSessionDetails(mainContent, req) {
    const sessionId = req.params.sid
    fetch(API_URL + `sessions/${sessionId}`)
        .then(res => {
            if (!res.ok) {
                mainContent.replaceChildren(p(null,"Session not found"))
            }
            else {
                return res.json()
            }
        })
        .then(session => {
            const h2Session = h2(null,"Session details")
            const list = ul(null,
                li(null, p(null, "Date : " + session.date)),
                li(null,
                    p(null, "Game : ",
                        a(`#games/${session.gameSession.gid}`, null, session.gameSession.name))
                ),
                li( null,p(null, "Capacity :" + session.capacity)),
                li(null,
                    p(null, "Players : "),
                    ul(null,
                        ...session.playersSession.map(player => li(null, a(`#players/${player.pid}`, null, player.name)))
                    )
                )
            )
            mainContent.replaceChildren(h2Session, list)
        })
}

function getPlayerDetails(mainContent, req) {
    const playerId = req.params.pid
    fetch(API_URL + `players/${playerId}`)
        .then(res => {
            if (!res.ok) {
                mainContent.replaceChildren(p(null,"Player not found"))
            }
            else {
                return res.json()
            }
        })
        .then(player => {
            const h2Player = h2(null, "Player details")
            const list = ul(null,
                li(null,"Name : " + player.name),
                li(null,"Number : " + player.pid),
                li(null,"Email : " + player.email)
            )
            const anchor = a(`#sessions/searchResults?pid=${player.pid}`, null,"Sessions with this player")
            mainContent.replaceChildren(h2Player, list, anchor)
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
    const time = document.getElementById('time').value;

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

    if (date && time) {
        const concatenatedDate = date + 'T' + time;
        queries.append('date', concatenatedDate);
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

    const divElement = div(null);

    if (skip >= limit) {
        const previousButton = button({type: "button"}, "Previous");
        previousButton.addEventListener('click', () => {
            window.location.href = generateUrl(limit, parseInt(skip) - limit);
        });
        divElement.appendChild(previousButton);
    }

    let queryStr = '';
    const urlParts = url.split('?');

    if (urlParts.length > 1 && urlParts[1].trim() !== '') {
        queryStr = url + `&limit=${limit}&skip=${parseInt(skip) + limit}`;
    } else {
        queryStr = url + `limit=${limit}&skip=${parseInt(skip) + limit}`;
    }

    fetch(queryStr)
        .then(res => {
            if (res.ok) {
                const nextButton = button({type: "button"}, "Next");
                nextButton.addEventListener('click', () => {
                    window.location.href = generateUrl(limit, parseInt(skip) + limit);
                });
                divElement.appendChild(nextButton);
            }
        })

    return divElement;

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