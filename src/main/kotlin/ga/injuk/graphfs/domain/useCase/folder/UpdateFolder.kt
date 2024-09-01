package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.useCase.UseCase

interface UpdateFolder : UseCase<UpdateFolder.Request, Unit> {
    override val name: String
        get() = UpdateFolder::class.java.name

    data class Request(
        val id: String,
        val driveId: String,
        val name: String?,
        val parentId: String?,
    )
}