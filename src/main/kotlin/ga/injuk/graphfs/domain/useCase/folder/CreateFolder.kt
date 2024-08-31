package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.Parent
import ga.injuk.graphfs.domain.useCase.UseCase

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