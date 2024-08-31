package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.ReactiveExtension.toList
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.folder.DeleteFolder
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import ga.injuk.graphfs.infrastructure.graph.ResourceDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteFolderImpl(
    private val folderDataAccess: FolderDataAccess,
    private val resourceDataAccess: ResourceDataAccess,
    private val settingClient: SettingClient,
) : DeleteFolder {
    @Transactional
    override suspend fun execute(user: User, request: DeleteFolder.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw NoSuchResourceException("there is no folder(${request.id}) in drive")

        resourceDataAccess.findResourcesOfDescendantsByFolderId(folder.id)
            .toList()
            .also { if (it.isNotEmpty()) throw IllegalStateException("there is ${it.size} resources exists in folder(${folder.id}) or sub-folders") }

        folderDataAccess.deleteAllDescendantsById(folder.id).await()
    }
}