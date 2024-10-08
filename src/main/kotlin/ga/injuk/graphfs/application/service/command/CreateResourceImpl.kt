package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.ReactiveExtension.await
import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Resource
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import ga.injuk.graphfs.domain.useCase.resource.CreateResource
import ga.injuk.graphfs.infrastructure.graph.FolderDataAccess
import ga.injuk.graphfs.infrastructure.graph.ResourceDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateResourceImpl(
    private val folderDataAccess: FolderDataAccess,
    private val resourceDataAccess: ResourceDataAccess,
    private val settingClient: SettingClient,
) : CreateResource {
    @Transactional
    override suspend fun execute(user: User, request: CreateResource.Request): CreateResource.Response {
        val drive = settingClient.getDriveInfo(user.project, request.driveId)

        val folder = folderDataAccess.findByDriveIdAndId(drive.id, request.folderId)
            .awaitSingleOrNull() ?: throw NoSuchResourceException("there is no folder(${request.folderId}) in drive")

        val resource = Resource.from(folder)
            .also { resourceDataAccess.save(it).await() }

        return CreateResource.Response(id = resource.id)
    }
}