import {a, div} from "../WebDSL/web_dsl.js";
import {gameSearchResult, sessionSearchResult,} from "./models.js";

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