package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.CreateFolder
import ga.injuk.graphfs.infrastructure.graph.DrivesDataAccess
import ga.injuk.graphfs.infrastructure.graph.FoldersDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class CreateFolderImpl(
    private val drivesDataAccess: DrivesDataAccess,
    private val foldersDataAccess: FoldersDataAccess,
    private val settingClient: SettingClient,
) : CreateFolder {
    override suspend fun execute(user: User, request: CreateFolder.Request): CreateFolder.Response {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val parent: Folder? = request.parent?.let { parent ->
            foldersDataAccess.findByDriveAndId(drive.id, parent.id)
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
            drivesDataAccess.save(drive.copy(children = drive.children + folder)).awaitSingleOrNull()
        } else {
            foldersDataAccess.save(parent.copy(children = parent.children + folder)).awaitSingleOrNull()
        }

        foldersDataAccess.save(folder).awaitSingleOrNull()

        return CreateFolder.Response(id = folder.id)
    }
}