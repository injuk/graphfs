package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.folder.UpdateFolder
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateFolderImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : UpdateFolder {
    @Transactional
    override suspend fun execute(user: User, request: UpdateFolder.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.id}) in drive")

        folder.copy(
            name = request.name,
        )
            .also { folderDataAccess.save(it).awaitSingleOrNull() }
    }
}