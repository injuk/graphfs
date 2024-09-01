package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.useCase.UseCase

interface ListElderFolders : UseCase<ListElderFolders.Request, List<Folder>> {
    override val name: String
        get() = ListElderFolders::class.java.name

    data class Request(
        val driveId: String,
        val id: String,
    )
}