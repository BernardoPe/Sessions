import {gameDetails} from "../../Models/Games/games.js";
import {sessionDetails, sessionDetailsAuthenticated} from "../../Models/Sessions/sessions.js";
import {playerDetails} from "../../Models/Players/players.js";

/**
 * Returns the game details view with the game's name, genres, and developer
 */
function gameDetailsView(game) {
	return gameDetails(game)
}

/**
 * Returns the session details view with the session's name, date, capacity, game and players
 */
function sessionDetailsView(session, authenticated) {
	return authenticated ? sessionDetailsAuthenticated(session)
						 : sessionDetails(session)
}

/**
 * Returns the player details view with the player's name and email
 */
function playerDetailsView(player) {
	return playerDetails(player)
}

export {gameDetailsView, sessionDetailsView, playerDetailsView}