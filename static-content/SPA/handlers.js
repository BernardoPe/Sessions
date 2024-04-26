import { homeView } from './Views/home.js';
import {gameSearchResultsView, sessionSearchResultsView} from "./Views/resultsViews.js";
import {gameDetailsView, playerDetailsView, sessionDetailsView} from "./Views/detailsViews.js";
import {sessionSearchView, gameSearchView} from "./Views/searchViews.js";
import {handleGamePagination, handleSessionPagination} from "./Scripts/pagination.js";
import {submitFormGameSearch, submitFormSessionSearch} from "./Scripts/formSubmit.js";
import {genericErrorView, notFoundView} from "./Views/errorViews.js";
import {loginView, registerView} from "./Views/authViews.js";
import {authLogin, authLogout, authRegister, getPlayerData} from "./Scripts/AuthHandling.js";
import {API_URL} from "../index.js";

export const RESULTS_PER_PAGE = 5;

function getHome(mainContent, req) {
    const user = getPlayerData()
    mainContent.replaceChildren(homeView(user));
}

function login(mainContent, req) {
    mainContent.replaceChildren(loginView());
    document.getElementById('loginForm').addEventListener('submit', authLogin)
}

function logout(mainContent, req) {
    authLogout()
}

function register(mainContent, req) {
    mainContent.replaceChildren(registerView());
    document.getElementById('registerForm').addEventListener('submit', authRegister)
}

function getGameSearch(mainContent, req) {
    mainContent.replaceChildren(gameSearchView());
    document.getElementById('gameSearchForm').addEventListener('submit', submitFormGameSearch);
}

function getGameSearchResults(mainContent, req) {
    const queries = new URLSearchParams();
    req.query.developer ? queries.append('developer', req.query.developer) : null;
    req.query.genres ? queries.append('genres', req.query.genres) : null;
    req.query.name ? queries.append('name', req.query.name) : null;
    const page = req.query.page ? req.query.page : 1;
    let queryStr = buildPaginationQuery(queries, page)
    if (page < 1) {
        const errView = notFoundView();
        mainContent.replaceChildren(errView)
        return
    }
    fetchWithHandling(API_URL + `games?${queryStr}`, mainContent, (searchResult) => {
        const gameResultsView = gameSearchResultsView(searchResult.games);
        gameResultsView.appendChild(handleGamePagination(queries, page, searchResult.total));
        mainContent.replaceChildren(gameResultsView);
    })
}


function buildPaginationQuery(queries, page) {
    let queryStr = queries.toString();
    const limit = RESULTS_PER_PAGE;
    const skip = (page - 1) * limit;
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
    req.query.date ? queries.append('date', req.query.date.replace(':', '_')) : null;
    const page = req.query.page ? req.query.page : 1;
    let queryStr = buildPaginationQuery(queries, page)

    if (page < 1) {
        const errView = notFoundView();
        mainContent.replaceChildren(errView)
        return
    }

    fetchWithHandling(API_URL + `sessions?${queryStr}`, mainContent, (searchResults) => {
        const searchResultsView = sessionSearchResultsView(searchResults.sessions);
        searchResultsView.appendChild(handleSessionPagination(queries, page, searchResults.total));
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
            if (res.ok) {
                if (res.status === 204)
                    return handleErrors(res, mainContent)
                return res.json().then(onSuccess)
            } else {
                handleErrors(res, mainContent)
            }
        })
}

function handleErrors(res, mainContent) {
    if (res.status === 204 || res.status === 404) {
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
    login,
    logout,
    register,
    getGameSearch,
    getSessionSearch,
    getGameSearchResults,
    getSessionSearchResults,
    getGameDetails,
    getSessionDetails,
    getPlayerDetails,
    RESULTS_PER_PAGE
}

