package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.CreateFolder
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
                .awaitSingleOrNull()
        }

        val folder = Folder.from(
            Folder.Request(
                name = request.name,
                parent = parent,
                createdBy = user,
            )
        )

        if (parent == null) {
            driveDataAccess.save(drive.copy(children = drive.children + folder)).awaitSingleOrNull()
        } else {
            folderDataAccess.save(parent.copy(children = parent.children + folder)).awaitSingleOrNull()
        }

        folderDataAccess.save(folder).awaitSingleOrNull()

        return CreateFolder.Response(id = folder.id)
    }
}