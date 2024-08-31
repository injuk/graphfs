package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.useCase.UseCase

interface GetFolder : UseCase<GetFolder.Request, GetFolder.Response> {
    override val name: String
        get() = GetFolder::class.java.name

    data class Request(
        val id: String,
        val driveId: String,
    )

    data class Response(
        val folder: Folder,
        val ancestors: List<Folder>,
    )
}