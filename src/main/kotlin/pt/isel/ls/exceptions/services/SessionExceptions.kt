package pt.isel.ls.exceptions.services

import pt.isel.ls.domain.session.Session
import pt.isel.ls.utils.Either

sealed class SessionCreationException {
    data object CapacityReachedLimit : SessionCreationException()

    data object CapacityNotLessOrEqualZero : SessionCreationException()

    data object GameNotFound : SessionCreationException()

    data object InvalidDate : SessionCreationException()
}

typealias SessionCreationResult = Either<SessionCreationException, Int>

sealed class SessionAddPlayerException {
    data object PlayerNotFound : SessionAddPlayerException()
}

typealias SessionAddPlayerResult = Either<SessionAddPlayerException, String>

sealed class SessionDetailsException {
    data object SessionNotFound : SessionDetailsException()
}

typealias SessionDetailsResult = Either<SessionDetailsException, Session>

sealed class SessionSearchException {
    data object GameNotFound : SessionSearchException()
}

typealias SessionSearchResult = Either<SessionSearchException, Set<Session>>