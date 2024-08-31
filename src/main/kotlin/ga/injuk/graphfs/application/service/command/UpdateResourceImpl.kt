package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.resource.UpdateResource
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import ga.injuk.graphfs.infrastructure.graph.ResourceDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateResourceImpl(
    private val folderDataAccess: FolderDataAccess,
    private val resourceDataAccess: ResourceDataAccess,
    private val settingClient: SettingClient,
) : UpdateResource {
    @Transactional
    override suspend fun execute(user: User, request: UpdateResource.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val oldFolder = folderDataAccess.findByDriveAndId(drive.id, request.folderId)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.folderId}) in drive")

        val resource = resourceDataAccess.findByFolderIdAndId(oldFolder.id, request.id)
            .awaitSingleOrNull() ?: throw java.lang.RuntimeException("there is no resource(${request.id}) in folder")

        val newFolder = folderDataAccess.findByDriveAndId(drive.id, request.newFolderId)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.newFolderId}) in drive")

        resourceDataAccess.moveFolder(resource.id, newFolder.id).await()
    }
}