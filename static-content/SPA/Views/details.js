import {gameDetails, playerDetails, sessionDetails} from "./Models/models.js";

/**
 * Returns the game details view with the game's name, genres, and developer
 */
function gameDetailsView(game) {
	return gameDetails(game)
}

/**
 * Returns the session details view with the session's name, date, capacity, game and players
 */
function sessionDetailsView(session) {
	return sessionDetails(session)
}

/**
 * Returns the player details view with the player's name and email
 */
function playerDetailsView(player) {
	return playerDetails(player)
}

export {gameDetailsView, sessionDetailsView, playerDetailsView}