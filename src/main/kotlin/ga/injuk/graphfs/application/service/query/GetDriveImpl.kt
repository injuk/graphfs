package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.application.ReactiveExtension.toList
import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.drive.GetDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class GetDriveImpl(
    private val driveDataAccess: DriveDataAccess,
    private val folderDataAccess: FolderDataAccess,
) : GetDrive {
    override suspend fun execute(user: User, request: GetDrive.Request): Drive {
        val drive = driveDataAccess.findByProjectIdAndId(user.project.id, request.id).awaitSingleOrNull()
            ?: throw NoSuchResourceException("there is no drive(${request.id}) in project")

        val roots = folderDataAccess.findRootsByProjectIdAndDriveId(user.project.id, drive.id)
            .toList()

        return drive.copy(children = roots)
    }
}