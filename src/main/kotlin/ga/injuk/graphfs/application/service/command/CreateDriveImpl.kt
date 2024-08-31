package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.drive.CreateDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateDriveImpl(
    private val driveDataAccess: DriveDataAccess,
) : CreateDrive {
    @Transactional
    override suspend fun execute(user: User, request: CreateDrive.Request): CreateDrive.Response {
        val (id) = Drive.from(
            Drive.Request(
                name = request.name,
                domain = request.domain,
                createdBy = user,
            )
        )
            .also { drive ->
                driveDataAccess.save(drive).awaitSingleOrNull()
            }

        return CreateDrive.Response(id)
    }
}