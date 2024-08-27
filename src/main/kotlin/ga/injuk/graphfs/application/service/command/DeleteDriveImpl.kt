package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.DeleteDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteDriveImpl(
    private val driveDataAccess: DriveDataAccess,
    private val folderDataAccess: FolderDataAccess,
) : DeleteDrive {
    @Transactional
    override suspend fun execute(user: User, request: DeleteDrive.Request) {
        val drive = driveDataAccess.findByProjectIdAndId(user.project.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no drive(${request.id}) in project")

        folderDataAccess.findRootsByProjectIdAndDriveId(user.project.id, request.id)
            .collectList()
            .awaitSingle()
            .also { if (it.isNotEmpty()) throw RuntimeException("cannot delete non-empty drive") }

        driveDataAccess.delete(drive).awaitSingleOrNull()
    }
}