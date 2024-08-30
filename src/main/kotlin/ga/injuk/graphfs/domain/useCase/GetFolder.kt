package ga.injuk.graphfs.domain.useCase

import ga.injuk.graphfs.domain.Folder

interface GetFolder : UseCase<GetFolder.Request, Folder> {
    override val name: String
        get() = GetFolder::class.java.name

    data class Request(
        val id: String,
        val driveId: String,
    )
}