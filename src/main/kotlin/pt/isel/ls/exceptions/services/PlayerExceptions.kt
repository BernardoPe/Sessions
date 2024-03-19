package pt.isel.ls.exceptions.services

import pt.isel.ls.domain.player.Player
import pt.isel.ls.utils.Either
import java.util.*

//sealed class UserCreationError {
//    object UserAlreadyExists : UserCreationError()
//    object InsecurePassword : UserCreationError()
//    object InsecureEmail : UserCreationError()
//}
//
//typealias UserCreationResult = Either<UserCreationError, Int>

sealed class PlayerCreationException {
    object PlayerAlreadyExists : PlayerCreationException()
    object EmailAlreadyExists : PlayerCreationException()
    object UnsafeEmail : PlayerCreationException()
}

typealias PlayerCreationResult = Either<PlayerCreationException, Pair<Int, UUID>>

sealed class PlayerDetailsException {
    object PlayerNotFound : PlayerDetailsException()
}

typealias PlayerDetailsResult = Either<PlayerDetailsException, Player>