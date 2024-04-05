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

import {a, br, button, div, fieldset, form, h1, h2, input, label, legend, li, ol, p, ul} from './WebDSL/web_dsl.js';

function getHome(mainContent, req) {
    const h2Element =
        h2(null, "Select the following options: ");
    const olElement =
        ol(null,
            li(null, a("#games/search", null, "Game Search")),
            li(null, a("#sessions/search", null, "Session Search")),
            li(null, a("#players/:pid", null, "Player Details"))
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
                label("limit", null, "Limit"),
                input({type: "number", id: "limit", step: "1", value: "5"}, "Limit"),
                label("skip", null, "Skip"),
                input({type: "number", id: "skip", step: "1", value: "0"}, "Skip"),
                button({type: "submit"}, "Search")
            )
        );
    mainContent.replaceChildren(forms);

    document.getElementById('gameSearchForm').addEventListener('submit', submitFormGameSearch);
}

function getGameSearchResults(mainContent, req) {
    const developer = req.query.developer;
    const genres = req.query.genres;
    // const limit = req.query.limit;
    // const skip = req.query.skip;
    fetch(API_URL + 'games?developer=' + developer + '&genres=' + genres)
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
                    p(null,
                        a("#games/" + g.gid, null, "Link Example to games/" + g.gid)
                    )
                )
            );
            mainContent.replaceChildren(divElement); // Append the results below the form
        })
}

function getSessionSearch(mainContent, req) {
    // TODO
}

function getSessionSearchResults(mainContent, req) {
    // TODO
}

function getGameDetails(mainContent, req) {
    // TODO
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
            const h2Player = h2("Player details")
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
    const limit = document.getElementById('limit').value;
    const skip = document.getElementById('skip').value;
    window.location.href = `#games/searchResults?developer=${developer}&genres=${genres}&limit=${limit}&skip=${skip}`;
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