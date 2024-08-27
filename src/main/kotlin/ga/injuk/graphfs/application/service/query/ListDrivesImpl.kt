package ga.injuk.graphfs.application.service.query

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.ListDrives
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class ListDrivesImpl(
    private val driveDataAccess: DriveDataAccess,
) : ListDrives {
    override suspend fun execute(user: User, request: ListDrives.Request): List<Drive> {
        val projectId = user.project.id

        return if (request.domain == null) {
            driveDataAccess.findByProjectId(projectId)
        } else {
            driveDataAccess.findByProjectIdAndDomain(projectId, request.domain)
        }.collectList().awaitSingle()
    }
}