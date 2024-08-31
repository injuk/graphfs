package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.useCase.UseCase

interface DeleteFolder : UseCase<DeleteFolder.Request, Unit> {
    override val name: String
        get() = DeleteFolder::class.java.name

    data class Request(
        val id: String,
        val driveId: String,
    )
}