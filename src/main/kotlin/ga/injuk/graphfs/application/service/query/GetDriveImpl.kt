package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.GetDrive
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import org.springframework.stereotype.Service

@Service
class GetDriveImpl(
    private val driveDataAccess: DriveDataAccess,
) : GetDrive {
    override suspend fun execute(user: User, request: GetDrive.Request): Drive {
        TODO("Not yet implemented")
    }
}