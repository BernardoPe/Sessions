package pt.isel.ls.exceptions.services

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.utils.Either

sealed class SessionCreationException {
    data object InvalidCapacity : SessionCreationException()

    data object GameNotFound : SessionCreationException()

    data object InvalidDate : SessionCreationException()
}

typealias SessionCreationResult = Either<SessionCreationException, UInt>

sealed class SessionAddPlayerException {
    data object SessionNotFound : SessionAddPlayerException()

    data object PlayerNotFound : SessionAddPlayerException()

    data object InvalidCapacity : SessionAddPlayerException()

    data object SessionFull : SessionAddPlayerException()
}

typealias SessionAddPlayerMessage = String

typealias SessionAddPlayerResult = Either<SessionAddPlayerException, SessionAddPlayerMessage>

sealed class SessionDetailsException {
    data object SessionNotFound : SessionDetailsException()
}

typealias SessionDetailsResult = Either<SessionDetailsException, Session>

sealed class SessionSearchException {
    data object GameNotFound : SessionSearchException()

    data object PLayerNotFound : SessionSearchException()

    data object InvalidDate : SessionSearchException()

}

typealias SessionIdentifier = UInt

typealias SessionList = List<Session>

typealias SessionSearchResult = Either<SessionSearchException, SessionList>

