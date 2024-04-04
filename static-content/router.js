const routes = []
let notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }

function addRouteHandler(path, handler){
    routes.push({path, handler})
}
function addDefaultNotFoundRouteHandler(notFoundRH) {
    notFoundRouteHandler = notFoundRH
}

function getRouteHandler(path){
    const route = matchPathScheme(path)
    return route ? route.handler : notFoundRouteHandler
}

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

function getRequestParamsAndQuery(path) {

    const route = matchPathScheme(path)
    if (!route) return {params: {}, query: {}}

    const routeParts = route.path.split("/")
    const pathParts = path.split("/")

    const params = {}
    for (let i = 0; i < routeParts.length; i++) {
        if (routeParts[i].startsWith(":")) {
            const key = routeParts[i].substring(1)
            params[key] = pathParts[i]
        }
    }

    const query = {}
    const queryIndex = path.indexOf("?")
    if (queryIndex !== -1) {
        const queryString = path.substring(queryIndex + 1)
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


