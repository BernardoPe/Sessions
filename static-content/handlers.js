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
    // TODO
}

function getPlayerDetails(mainContent, req) {
    // TODO
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