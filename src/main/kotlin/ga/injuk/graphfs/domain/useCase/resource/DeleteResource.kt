package ga.injuk.graphfs.domain.useCase.resource

import ga.injuk.graphfs.domain.useCase.UseCase

interface DeleteResource : UseCase<DeleteResource.Request, Unit> {
    override val name: String
        get() = DeleteResource::class.java.name

    data class Request(
        val driveId: String,
        val folderId: String,
        val id: String,
    )
}