import {homeView} from './Views/Pages/Home/home.js';
import {gameSearchResultsView, sessionSearchResultsView} from "./Views/Pages/Results/results.js";
import {gameDetailsView, playerDetailsView, sessionDetailsView} from "./Views/Pages/Details/details.js";
import {gameSearchView, sessionSearchView} from "./Views/Pages/Search/search.js";
import {gameCreateView, sessionCreateView} from "./Views/Pages/Create/create.js";
import {handleGamePagination, handleSessionPagination} from "./Scripts/pagination.js";
import {genericErrorView, notFoundView} from "./Views/Pages/Error/error.js";
import {loginView, registerView} from "./Views/Pages/Auth/auth.js";
import {authLogout, getPlayerData} from "./Scripts/auth.js";
import {
    submitFormGameSearch,
    submitFormSessionSearch
} from "./Scripts/formSubmit.js"; // do not remove, messes up the searches url for some reason
import {API_URL, GAMES_URL} from "../index.js";

export const RESULTS_PER_PAGE = 10;

/**
 * Handles the routing of the application to the home page
 */
function getHome(mainContent, req) {
    const user = getPlayerData()
    mainContent.replaceChildren(homeView(user));
}

/**
 * Handles the routing of the application to the login page
 */
function login(mainContent, req) {
    mainContent.replaceChildren(loginView());
}

/**
 * Handles the routing of the application for logging out
 */
function logout(mainContent, req) {
    authLogout()
}

/**
 * Handles the routing of the application to the registration page
 */
function register(mainContent, req) {
    mainContent.replaceChildren(registerView());
}

/**
 * Handles the routing of the application to the game search page
 */
function getGameSearch(mainContent, req) {
    mainContent.replaceChildren(gameSearchView());
}

/**
 * Handles the routing of the application to the game search results page
 */
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
    fetchWithHandling(API_URL + GAMES_URL + `?${queryStr}`, mainContent, (searchResult) => {
        const gameResultsView = gameSearchResultsView(searchResult.games);
        gameResultsView.appendChild(handleGamePagination(queries, page, searchResult.total));
        mainContent.replaceChildren(gameResultsView);
    })
}


/**
 * Builds the query string for pagination
 */
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

/**
 * Handles the routing of the application to the session search page
 */
function getSessionSearch(mainContent, req) {
    mainContent.replaceChildren(sessionSearchView());
}

/**
 * Handles the routing of the application to the session search results page
 */
function getSessionSearchResults(mainContent, req) {

    const queries = new URLSearchParams();
    req.query.gid ? queries.append('gid', req.query.gid) : null;
    req.query.pid ? queries.append('pid', req.query.pid) : null;
    req.query.state ? queries.append('state', req.query.state) : null;
    req.query.date ? queries.append('date', req.query.date) : null;
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

/**
 * Handles the routing of the application to the game details page
 */
function getGameDetails(mainContent, req) {
    const gameId = req.params.gid
    fetchWithHandling(API_URL + `games/${gameId}`, mainContent, (game) => {
        const gameView = gameDetailsView(game)
        mainContent.replaceChildren(gameView)
    })
}

/**
 * Handles the routing of the application to the session details page
 */
function getSessionDetails(mainContent, req) {
    const sessionId = req.params.sid
    const authenticated = getPlayerData()
    fetchWithHandling(API_URL + `sessions/${sessionId}`, mainContent, (session) => {
        const sessionView = sessionDetailsView(session, authenticated)
        mainContent.replaceChildren(sessionView)
    })
}

/**
 * Handles the routing of the application to the player details page
 */
function getPlayerDetails(mainContent, req) {
    const playerId = req.params.pid
    fetchWithHandling(API_URL + `players/${playerId}`, mainContent, (player) => {
        const playerView = playerDetailsView(player)
        mainContent.replaceChildren(playerView)
    })
}

/**
 * Handles the routing of the application to the game creation page
 * If the user is not logged in, they are redirected to the login page
 */
function createGame(mainContent, req) {
    if (!getPlayerData()) return window.location.replace('#login')
    mainContent.replaceChildren(gameCreateView());
}

/**
 * Handles the routing of the application to the session creation page
 * If the user is not logged in, they are redirected to the login page
 */
function createSession(mainContent, req) {
    if (!getPlayerData()) return window.location.replace('#login')
    mainContent.replaceChildren(sessionCreateView());
}

/**
 * Handles API requests
 *
 * If an error occurs, it is handled and an error view is displayed
 *
 * @param url
 * @param mainContent
 * @param onSuccess
 */
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

/**
 * Handles errors that occur during API requests
 *
 * If the status code is 204 or 404, a not found view is displayed
 * Otherwise, a generic error view is displayed
 */
function handleErrors(res, mainContent) {
    if (res.status === 204 || res.status === 404) {
        const errView = notFoundView();
        mainContent.replaceChildren(errView)
    } else {
        const errView = genericErrorView();
        mainContent.replaceChildren(errView)
    }
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
    createGame,
    createSession,
    RESULTS_PER_PAGE
}

