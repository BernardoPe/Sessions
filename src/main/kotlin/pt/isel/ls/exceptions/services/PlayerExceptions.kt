package pt.isel.ls.exceptions.services

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.utils.Either
import java.util.*

sealed class PlayerCreationException {
    data object EmailAlreadyExists : PlayerCreationException()
    data object UnsafeEmail : PlayerCreationException()
}

typealias PlayerCreationResult = Either<PlayerCreationException, PlayerCredentials>

sealed class PlayerDetailsException {
    data object PlayerNotFound : PlayerDetailsException()
}

typealias PlayerDetailsResult = Either<PlayerDetailsException, Player>


typealias PlayerCredentials = Pair<UInt, UUID>