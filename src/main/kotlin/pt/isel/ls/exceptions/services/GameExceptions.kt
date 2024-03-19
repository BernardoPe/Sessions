package pt.isel.ls.exceptions.services

import pt.isel.ls.domain.game.Game
import pt.isel.ls.utils.Either

sealed class GameCreationException {
    data object GameNameAlreadyExists : GameCreationException()
}

typealias GameCreationResult = Either<GameCreationException, Int>

sealed class GameDetailsException {
    data object GameNotFound : GameDetailsException()
}

typealias GameDetailsResult = Either<GameDetailsException, Game>

sealed class GameSearchException {
    data object GenresNotFound : GameSearchException()

    data object DeveloperNotFound : GameSearchException()
}

typealias GameSearchResult = Either<GameSearchException, Set<Game>>