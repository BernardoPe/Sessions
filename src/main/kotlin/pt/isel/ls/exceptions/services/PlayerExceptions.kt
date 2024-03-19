package pt.isel.ls.exceptions.services

import pt.isel.ls.domain.player.Player
import pt.isel.ls.utils.Either
import java.util.*

sealed class PlayerCreationException {
    data object PlayerAlreadyExists : PlayerCreationException()
    data object EmailAlreadyExists : PlayerCreationException()
    data object UnsafeEmail : PlayerCreationException()
}

typealias PlayerCreationResult = Either<PlayerCreationException, Pair<Int, UUID>>

sealed class PlayerDetailsException {
    data object PlayerNotFound : PlayerDetailsException()
}

typealias PlayerDetailsResult = Either<PlayerDetailsException, Player>