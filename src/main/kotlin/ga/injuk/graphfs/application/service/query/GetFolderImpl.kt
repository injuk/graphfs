package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.application.ReactiveExtension.toList
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.GetFolder
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class GetFolderImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : GetFolder {
    override suspend fun execute(user: User, request: GetFolder.Request): Folder {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.id}) in drive")

        val children = folderDataAccess.findChildrenById(folder.id)
            .toList()

        // TODO: resource 목록도 함께 반환하도록 개선할 것

        return folder.copy(children = children)
    }
}