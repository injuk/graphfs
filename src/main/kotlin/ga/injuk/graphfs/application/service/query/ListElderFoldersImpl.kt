package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.application.ReactiveExtension.toList
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.folder.ListElderFolders
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class ListElderFoldersImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : ListElderFolders {
    override suspend fun execute(user: User, request: ListElderFolders.Request): List<Folder> {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveIdAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw NoSuchResourceException("there is no folder(${request.id}) in drive")

        return folderDataAccess.findEldersByDriveIdAndId(drive.id, folder.id)
            .toList()
    }
}