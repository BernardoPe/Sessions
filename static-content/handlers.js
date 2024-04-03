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
    // TODO
}

function getGameSearch(mainContent, req) {
    // TODO
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