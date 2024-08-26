package ga.injuk.graphfs.domain.useCase

import ga.injuk.graphfs.domain.Parent

interface CreateFolder : UseCase<CreateFolder.Request, CreateFolder.Response> {
    override val name: String
        get() = CreateFolder::class.java.name

    data class Request(
        val driveId: String,
        val name: String,
        val parent: Parent?,
    )

    data class Response(
        val id: String,
    )
}