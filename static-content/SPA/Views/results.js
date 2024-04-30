import {a, div} from "../WebDSL/web_dsl.js";
import {gameSearchResult, sessionSearchResult,} from "./Models/models.js";

/**
 * View for the search results of sessions
 *
 * @param sessions - the sessions to display
 */
function sessionSearchResultsView(sessions) {
    return div(null,
        div({class: "search-results-container"},
            ...sessions.map(s =>
                sessionSearchResult(s)
            ),
		),
        a("#sessions/search", {class: "session__reference"}, "Search for more sessions"),
    )
}

/**
 * View for the search results of games
 * @param games - the games to display
 */
function gameSearchResultsView(games) {
    return div(null,
        div({class: "search-results-container"},
            ...games.map(g =>
                gameSearchResult(g)
            ),
        )
    )
}

export {sessionSearchResultsView, gameSearchResultsView};