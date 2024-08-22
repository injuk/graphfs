package ga.injuk.graphfs.domain.useCase

import ga.injuk.graphfs.domain.User

interface UseCase<Request, Response> {
    val name: String

    suspend fun execute(user: User, request: Request): Response
}