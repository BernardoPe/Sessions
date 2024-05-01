import {a, div, svg, path} from "../WebDSL/web_dsl.js";
import {gameSearchResult, sessionSearchResult,} from "./Models/models.js";

/**
 * View for the search results of sessions
 *
 * @param sessions - the sessions to display
 */
function sessionSearchResultsView(sessions) {
    return div(null,
        a("#sessions/search", {class: "return_reference"}, 
            svg({viewBox: "0 0 9 15"},
                path({d: "M8.339 7.37L1.619.604.04 2.195l5.142 5.18-5.142 5.172 1.58 1.593 6.72-6.77z", fill: "currentColor"}
            ),
        )),
        div({class: "search-results-container"},
            ...sessions.map(s =>
                sessionSearchResult(s)
            ),
		),
    )
}

/**
 * View for the search results of games
 * @param games - the games to display
 */
function gameSearchResultsView(games) {
    return div(null,
        a("#games/search", {class: "return_reference"},  
            svg({viewBox: "0 0 9 15"},
                path({d: "M8.339 7.37L1.619.604.04 2.195l5.142 5.18-5.142 5.172 1.58 1.593 6.72-6.77z", fill: "currentColor"}
            ),
        )),
        div({class: "search-results-container"},
            ...games.map(g =>
                gameSearchResult(g)
            ),
        ),
    )
}

export {sessionSearchResultsView, gameSearchResultsView};