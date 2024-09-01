package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.folder.UpdateFolder
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateFolderImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : UpdateFolder {
    companion object {
        private val logger = LoggerFactory.getLogger(UpdateFolderImpl::class.java)
    }

    @Transactional
    override suspend fun execute(user: User, request: UpdateFolder.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveIdAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw NoSuchResourceException("there is no folder(${request.id}) in drive")

        if (request.name != null) {
            folder.copy(
                name = request.name,
            )
                .also { folderDataAccess.save(it).await() }
        }

        if (request.parentId == null) {
            folderDataAccess.updateParentToDriveByDriveIdAndId(drive.id, request.id)
                .await()
        } else if (request.parentId != folder.parentId) {
            val newParent = folderDataAccess.findByDriveIdAndId(drive.id, request.parentId)
                .awaitSingleOrNull()
                ?: throw NoSuchResourceException("there is no folder(${request.parentId}) in drive")

            folderDataAccess.updateParentById(targetId = folder.id, newParentId = newParent.id)
                .await()
        } else {
            logger.debug("requested parent id is same to current parent... do nothing")
        }
    }
}