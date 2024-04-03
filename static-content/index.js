import router from "./router.js";
import handlers from "./handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)


/**
 * Handler for the load event. This function is called when the page is loaded.
 * It sets up the routes and their handlers.
 *
 * Routes were defined using the express.js syntax, with the addition of the colon (:) character to indicate a parameter.
 */
function loadHandler(){

    // home page
    router.addRouteHandler("home", handlers.getHome)
    // search bar page
    router.addRouteHandler("games/search", handlers.getGameSearch)
    // search bar page
    router.addRouteHandler("sessions/search", handlers.getSessionSearch)
    // page with search results, with search query parameters in the URL query string
    router.addRouteHandler("games/searchResults", handlers.getGameSearchResults)
    // page with search results, with search query parameters in the URL query string
    router.addRouteHandler("sessions/searchResults", handlers.getSessionSearchResults)
    // page with game details
    router.addRouteHandler("games/:gid", handlers.getGameDetails)
    // page with session details
    router.addRouteHandler("sessions/:sid", handlers.getSessionDetails)
    // page with player details
    router.addRouteHandler("players/:pid", handlers.getPlayerDetails)

    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

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