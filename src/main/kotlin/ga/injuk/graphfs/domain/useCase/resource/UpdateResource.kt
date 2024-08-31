package ga.injuk.graphfs.domain.useCase.resource

import ga.injuk.graphfs.domain.useCase.UseCase

interface UpdateResource : UseCase<UpdateResource.Request, Unit> {
    override val name: String
        get() = UpdateResource::class.java.name

    data class Request(
        val driveId: String,
        val folderId: String,
        val id: String,

        val newFolderId: String,
    )
}