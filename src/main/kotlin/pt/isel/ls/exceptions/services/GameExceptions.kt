package pt.isel.ls.exceptions.services

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.utils.Either

sealed class GameCreationException {
    data object GameNameAlreadyExists : GameCreationException()
}

typealias GameCreationResult = Either<GameCreationException, UInt>

sealed class GameDetailsException {
    data object GameNotFound : GameDetailsException()
}

typealias GameDetailsResult = Either<GameDetailsException, Game>

sealed class GameSearchException {
    data object GenresNotFound : GameSearchException()

    data object DeveloperNotFound : GameSearchException()
}

typealias GameIdentifier = UInt

typealias GameList = List<Game>

typealias GameSearchResult = Either<GameSearchException, GameList>

