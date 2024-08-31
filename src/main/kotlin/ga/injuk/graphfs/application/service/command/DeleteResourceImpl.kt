package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.resource.DeleteResource
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import ga.injuk.graphfs.infrastructure.graph.ResourceDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteResourceImpl(
    private val folderDataAccess: FolderDataAccess,
    private val resourceDataAccess: ResourceDataAccess,
    private val settingClient: SettingClient,
) : DeleteResource {
    @Transactional
    override suspend fun execute(user: User, request: DeleteResource.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveAndId(drive.id, request.folderId)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.folderId}) in drive")

        val resource = resourceDataAccess.findByFolderIdAndId(folder.id, request.id)
            .awaitSingleOrNull() ?: throw java.lang.RuntimeException("there is no resource(${request.id}) in folder")

        resourceDataAccess.delete(resource).await()
    }
}