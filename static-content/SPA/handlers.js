import { homeView } from './Views/home.js';
import {gameSearchResultsView, sessionSearchResultsView} from "./Views/resultsViews.js";
import {gameDetailsView, playerDetailsView, sessionDetailsView} from "./Views/detailsViews.js";
import {sessionSearchView, gameSearchView} from "./Views/searchViews.js";
import {handleGamePagination, handleSessionPagination} from "./Scripts/pagination.js";
import {submitFormGameSearch, submitFormSessionSearch} from "./Scripts/formSubmit.js";
import {genericErrorView, notFoundView} from "./Views/errorViews.js";

const API_URL = 'http://localhost:8080/';

const RESULTS_PER_PAGE = 2;

function getHome(mainContent, req) {
    mainContent.replaceChildren(homeView());
}

function getGameSearch(mainContent, req) {
    mainContent.replaceChildren(gameSearchView());
    document.getElementById('gameSearchForm').addEventListener('submit', submitFormGameSearch);
}

function getGameSearchResults(mainContent, req) {
    const queries = new URLSearchParams();
    req.query.developer ? queries.append('developer', req.query.developer) : null;
    req.query.genres ? queries.append('genres', req.query.genres) : null;
    const limit = req.query.limit ? req.query.limit : RESULTS_PER_PAGE;
    const skip = req.query.skip ? req.query.skip : 0;
    let queryStr = buildPaginationQuery(queries, limit, skip)
    fetchWithHandling(API_URL + `games?${queryStr}`, mainContent, (games) => {
        const gameResultsView = gameSearchResultsView(games);
        gameResultsView.appendChild(handleGamePagination(queries, limit, skip));
        mainContent.replaceChildren(gameResultsView);
    })
}


function buildPaginationQuery(queries, limit, skip) {
    let queryStr = queries.toString();

    if (queryStr.length === 0) {
        queryStr = `limit=${limit}&skip=${skip}`;
    } else {
        queryStr += `&limit=${limit}&skip=${skip}`;
    }

    return queryStr;
}


function getSessionSearch(mainContent, req) {
    mainContent.replaceChildren(sessionSearchView());
    document.getElementById('sessionSearchForm').addEventListener('submit', submitFormSessionSearch);
}

function getSessionSearchResults(mainContent, req) {

    const queries = new URLSearchParams();
    req.query.gid ? queries.append('gid', req.query.gid) : null;
    req.query.pid ? queries.append('pid', req.query.pid) : null;
    req.query.state ? queries.append('state', req.query.state) : null;
    req.query.date ? queries.append('date', req.query.date) : null;
    const limit = req.query.limit ? req.query.limit : RESULTS_PER_PAGE;
    const skip = req.query.skip ? req.query.skip : 0;
    let queryStr = buildPaginationQuery(queries, limit, skip)

    fetchWithHandling(API_URL + `sessions?${queryStr}`, mainContent, (sessions) => {
        const searchResultsView = sessionSearchResultsView(sessions);
        searchResultsView.appendChild(handleSessionPagination(queries, limit, skip));
        mainContent.replaceChildren(searchResultsView);
    })

}

function getGameDetails(mainContent, req) {
    const gameId = req.params.gid
    fetchWithHandling(API_URL + `games/${gameId}`, mainContent, (game) => {
        const gameView = gameDetailsView(game)
        mainContent.replaceChildren(gameView)
    })
}

function getSessionDetails(mainContent, req) {
    const sessionId = req.params.sid
    fetchWithHandling(API_URL + `sessions/${sessionId}`, mainContent, (session) => {
        const sessionView = sessionDetailsView(session)
        mainContent.replaceChildren(sessionView)
    })
}

function getPlayerDetails(mainContent, req) {
    const playerId = req.params.pid
    fetchWithHandling(API_URL + `players/${playerId}`, mainContent, (player) => {
        const playerView = playerDetailsView(player)
        mainContent.replaceChildren(playerView)
    })
}

function fetchWithHandling(url, mainContent, onSuccess) {
    fetch(url)
        .then(res => {
            res.ok ? res.json().then(res => onSuccess(res)) : handleErrors(res, mainContent)
        })
}

function handleErrors(res, mainContent) {
    if (res.status === 404) {
        const errView = notFoundView();
        mainContent.replaceChildren(errView)
    } else {
        const errView = genericErrorView();
        mainContent.replaceChildren(errView)
    }
    document.querySelector('button')
        .addEventListener('click', () => window.history.back())
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

export {API_URL}