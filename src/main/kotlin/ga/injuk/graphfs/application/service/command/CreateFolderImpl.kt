package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.folder.CreateFolder
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateFolderImpl(
    private val driveDataAccess: DriveDataAccess,
    private val folderDataAccess: FolderDataAccess,
    private val settingClient: SettingClient,
) : CreateFolder {
    @Transactional
    override suspend fun execute(user: User, request: CreateFolder.Request): CreateFolder.Response {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val parent: Folder? = request.parent?.let { parent ->
            folderDataAccess.findByDriveAndId(drive.id, parent.id)
                .awaitSingleOrNull() ?: throw NoSuchResourceException("there is no folder(${parent.id}) in drive")
        }

        val folder = Folder.from(
            Folder.Request(
                name = request.name,
                parent = parent,
                createdBy = user,
            )
        )
            .also {
                if (it.isRootFolder()) {
                    driveDataAccess.save(drive.copy(children = drive.children + it))
                } else {
                    folderDataAccess.save(parent!!.copy(children = parent.children + it))
                }.await()
            }

        folderDataAccess.save(folder).await()

        return CreateFolder.Response(id = folder.id)
    }
}