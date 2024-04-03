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
import { body, div, ul, li, a, button, input, h1, h2, h3, h4, h5, h6, p, span, img, br, ol, _test } from './WebDSL/web_dsl.js';
function getHome(mainContent, req) {
    const h2 = document.createElement("h2")
    const selectionText = document.createTextNode("Select the following options: ")
    const gameSearchText = document.createTextNode("Game Search")
    const sessionSearchText = document.createTextNode("Session Search")
    const playerText = document.createTextNode("Player Details")
    const ol = document.createElement("OL")
    const liGames = document.createElement("LI")
    const liSessions = document.createElement("LI")
    const liPlayer = document.createElement("LI")
    const aGames = document.createElement("a")
    const aSessions = document.createElement("a")
    const aPlayer = document.createElement("a")
    h2.appendChild(selectionText)
    liGames.appendChild(aGames)
    liSessions.appendChild(aSessions)
    liPlayer.appendChild(aPlayer)
    aGames.appendChild(gameSearchText)
    liSessions.appendChild(sessionSearchText)
    liPlayer.appendChild(playerText)
    aGames.href = "#games/search"
    aSessions.href = "#sessions/search"
    aPlayer.href = "#players/:pid"
    ol.appendChild(liGames)
    ol.appendChild(liSessions)
    ol.appendChild(liPlayer)
    mainContent.replaceChildren(h2)
    mainContent.replaceChildren(ol)
}

function getGameSearch(mainContent, req) {
    fetch(API_BASE_URL + "games/search", {body: JSON.stringify(req)})
        .then(res => res.json())
        .then(games => {
            const div = document.createElement("div")

            const h1 = document.createElement("h1")
            const text = document.createTextNode("Game Search")
            h1.appendChild(text)
            div.appendChild(h1)

            games.forEach(s => {
                const p = document.createElement("p")
                const a = document.createElement("a")
                const aText = document.createTextNode("Link Example to games/" + s.number);
                a.appendChild(aText)
                a.href = "#games/search/" + s.number
                p.appendChild(a)
                div.appendChild(p)
            })
            mainContent.replaceChildren(div)
        })
}

function getSessionSearch(mainContent, req) {
    // TODO
}

function getGameSearchResults(mainContent, req) {
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