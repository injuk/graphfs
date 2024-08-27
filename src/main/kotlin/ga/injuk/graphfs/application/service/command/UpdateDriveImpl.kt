package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.UpdateDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class UpdateDriveImpl(
    private val driveDataAccess: DriveDataAccess,
) : UpdateDrive {
    override suspend fun execute(user: User, request: UpdateDrive.Request) {
        val drive = driveDataAccess.findByProjectIdAndId(user.project.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no drive(${request.id}) in project")

        drive.copy(
            name = request.name,
        )
            .also { driveDataAccess.save(it).awaitSingleOrNull() }
    }
}