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
    const h1 = document.createElement("h1")
    const text = document.createTextNode("Home")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function getGameSearch(mainContent, req) {
    fetch(API_BASE_URL + "games/search")
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