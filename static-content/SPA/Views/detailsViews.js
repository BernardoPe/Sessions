import {gameDetails, playerDetails, sessionDetails} from "./models.js";

function gameDetailsView(game) {
	return gameDetails(game)
}

function sessionDetailsView(session) {
	return sessionDetails(session)
}

function playerDetailsView(player) {
	return playerDetails(player)
}

export {gameDetailsView, sessionDetailsView, playerDetailsView}