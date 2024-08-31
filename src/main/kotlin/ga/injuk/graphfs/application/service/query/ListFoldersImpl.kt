package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.application.ReactiveExtension.toList
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.folder.ListFolders
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import org.springframework.stereotype.Service

@Service
class ListFoldersImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : ListFolders {
    override suspend fun execute(user: User, request: ListFolders.Request): List<Folder> {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        return folderDataAccess.findAllByDriveId(drive.id)
            .toList()
    }
}