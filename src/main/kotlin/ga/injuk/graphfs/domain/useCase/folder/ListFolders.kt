package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.useCase.UseCase

interface ListFolders : UseCase<ListFolders.Request, List<Folder>> {
    override val name: String
        get() = ListFolders::class.java.name

    data class Request(
        val driveId: String,
        val name: String?,
    )
}