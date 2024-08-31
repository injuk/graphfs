package ga.injuk.graphfs.domain.useCase.resource

import ga.injuk.graphfs.domain.useCase.UseCase

interface CreateResource : UseCase<CreateResource.Request, CreateResource.Response> {
    override val name: String
        get() = CreateResource::class.java.name

    data class Request(
        val driveId: String,
        val folderId: String,
    )

    data class Response(
        val id: String,
    )
}