import router from "./SPA/router.js";
import handlers from "./SPA/handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

const HOME_URL = 'home'
const GAMES_URL = 'games'
const SESSIONS_URL = 'sessions'
const SESSION_DETAILS_URL = 'sessions/:sid'
const GAMES_SEARCH_URL = 'games/search'
const GAME_DETAILS_URL = 'games/:gid'
const SESSIONS_SEARCH_URL = 'sessions/search'
const PLAYER_DETAILS_URL = 'players/:pid'
const PLAYERS_URL = 'players'
const LOGIN_URL = 'login'
const REGISTER_URL = 'register'
const LOGOUT_URL = 'logout'
const API_URL = 'http://localhost:8080/';


/**
 * Handler for the load event. This function is called when the page is loaded.
 * It sets up the routes and their handlers.
 *
 * Routes were defined using the express.js syntax, with the addition of the colon (:) character to indicate a parameter.
 */
function loadHandler(){
    // home page
    router.addRouteHandler(HOME_URL, handlers.getHome)
    // search bar page
    router.addRouteHandler(GAMES_SEARCH_URL, handlers.getGameSearch)
    // search bar page
    router.addRouteHandler(SESSIONS_SEARCH_URL, handlers.getSessionSearch)
    // page with search results, with search query parameters in the URL query string
    router.addRouteHandler(GAMES_URL, handlers.getGameSearchResults)
    // page with search results, with search query parameters in the URL query string
    router.addRouteHandler(SESSIONS_URL, handlers.getSessionSearchResults)
    // page with game details
    router.addRouteHandler(GAME_DETAILS_URL, handlers.getGameDetails)
    // page with session details
    router.addRouteHandler(SESSION_DETAILS_URL, handlers.getSessionDetails)
    // page with player details
    router.addRouteHandler(PLAYER_DETAILS_URL, handlers.getPlayerDetails)

    router.addRouteHandler(LOGIN_URL, handlers.login)
    router.addRouteHandler(REGISTER_URL, handlers.register)
    router.addRouteHandler(LOGOUT_URL, handlers.logout)

    router.addDefaultNotFoundRouteHandler(() => window.location.hash = HOME_URL)

    hashChangeHandler()
}

/**
 * Handler for the hashchange event. This function is called when the URL hash changes.
 * It gets the path from the URL hash and calls the appropriate route handler.
 */
function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path =  window.location.hash.replace("#", "")
    const handler = router.getRouteHandler(path)
    const req = router.getRequestParamsAndQuery(path)
    handler(mainContent, req)
}

export {
    API_URL,
    HOME_URL,
    GAMES_URL,
    SESSIONS_URL,
    SESSION_DETAILS_URL,
    GAMES_SEARCH_URL,
    GAME_DETAILS_URL,
    SESSIONS_SEARCH_URL,
    PLAYER_DETAILS_URL,
    PLAYERS_URL,
    LOGIN_URL,
    REGISTER_URL,
    LOGOUT_URL
}