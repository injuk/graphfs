package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.folder.DeleteFolder
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteFolderImpl(
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : DeleteFolder {
    @Transactional
    override suspend fun execute(user: User, request: DeleteFolder.Request) {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveAndId(drive.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no folder(${request.id}) in drive")

        // TODO: 리소스를 갖고 있는 폴더는 제거할 수 없도록 개선 필요

        folderDataAccess.deleteAllDescendantsById(folder.id).await()
    }
}