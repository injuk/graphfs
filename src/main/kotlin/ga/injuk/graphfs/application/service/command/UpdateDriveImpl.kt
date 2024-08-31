package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.drive.UpdateDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateDriveImpl(
    private val driveDataAccess: DriveDataAccess,
) : UpdateDrive {
    @Transactional
    override suspend fun execute(user: User, request: UpdateDrive.Request) {
        val drive = driveDataAccess.findByProjectIdAndId(user.project.id, request.id)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no drive(${request.id}) in project")

        drive.copy(
            name = request.name,
        )
            .also { driveDataAccess.save(it).await() }
    }
}