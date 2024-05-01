/**
 * Represents the routes and their respective handlers in the application
 * Each route is an object with a URI path and its handler
 *
 * When a route is requested, the router will match the path to the route and return the handler for that route
 *
 * If no route is found, the router will return a default not found route handler, that sends the user to the home page
 */
const routes = []

let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

/**
 * Adds the routes and their handlers to the routes array
 * @param path
 * @param handler
 */
function addRouteHandler(path, handler){
    if (hasConflictingRoute(path)) throw `Route ${path} conflicts with another route`
    routes.push({path, handler})
}


/**
 * Adds the default not found route handler
 */
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

/**
 * Returns the route handler for the given path
 */
function getRouteHandler(path){
    const route = matchPathScheme(path)
    return route ? route.handler : notFoundRouteHandler
}

/**
 * Finds the route that matches the path scheme
 *
 * If the route is parameterized, the path is matched against the route scheme, ignoring parameter
 * segments and returning the route if the path matches the scheme
 *
 * @param path
 * @returns {*}
 */
function matchPathScheme(path) {

    const pathWithoutQuery =
        path.includes("?") ? path.substring(0, path.indexOf("?")) : path;

    return routes.find(route => {
        const routeParts = route.path.split("/")
        const pathParts = pathWithoutQuery.split("/")
        if (routeParts.length !== pathParts.length) return false
        for (let i = 0; i < routeParts.length; i++) {
            if (routeParts[i].startsWith(":")) continue
            if (routeParts[i] !== pathParts[i]) return false
        }
        return true
    })
}

/**
 * Checks if the path conflicts with an existing route
 */
function hasConflictingRoute(path) {
    return matchPathScheme(path) || routes.find(route => route.path === path)
}

/**
 * Returns the parameters and query from the path
 *
 * The parameters are extracted from the path based on the route scheme
 * The query is extracted from the path based on the query string
 *
 * @param path
 * @returns {{params: {}, query: {}}}
 */
function getRequestParamsAndQuery(path) {

    const route = matchPathScheme(path)
    if (!route) return {params: {}, query: {}}

    const decodedPath = decodeURIComponent(path)

    const routeParts = route.path.split("/");
    const pathParts = decodedPath.split('?')[0].split("/");

    const params = {}
    for (let i = 0; i < routeParts.length; i++) {
        if (routeParts[i].startsWith(":")) {
            const key = routeParts[i].substring(1)
            params[key] = pathParts[i]
        }
    }

    const query = {}
    const queryIndex = decodedPath.indexOf("?")
    if (queryIndex !== -1) {
        const queryString = decodedPath.substring(queryIndex + 1)
        const queryParts = queryString.split("&")
        queryParts.forEach(part => {
            const [key, value] = part.split("=")
            if (value.includes(',')) {
                query[key] = value.split(',')
            } else {
                query[key] = value
            }
        })
    }
    return {params, query}
}

const router = {
    addRouteHandler,
    getRouteHandler,
    addDefaultNotFoundRouteHandler,
    getRequestParamsAndQuery
}

export default router


