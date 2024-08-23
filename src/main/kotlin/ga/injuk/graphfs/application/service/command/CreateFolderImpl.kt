package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.CreateFolder
import ga.injuk.graphfs.infrastructure.graph.Neo4jDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class CreateFolderImpl(
    private val repository: Neo4jDataAccess,
    private val settingClient: SettingClient,
) : CreateFolder {
    override suspend fun execute(user: User, request: CreateFolder.Request): CreateFolder.Response {
        val rootFolder = settingClient.getRootFolder(domain = request.domain, project = user.project)
            ?: throw RuntimeException("there is no root folder in project${user.project.id}")

        val parent: Folder = if (request.parent.id == rootFolder.info.id) {
            rootFolder.info
        } else {
            repository.findOneById(rootFolder.info.id, request.parent.id)
                .awaitSingleOrNull()
        } ?: throw RuntimeException("there is no folder with id ${request.parent.id}")

        val folder = Folder.from(
            request = Folder.Request(
                name = request.name,
                parent = parent,
                createdBy = user,
            ),
        )

        repository.save(folder).awaitSingleOrNull()

        return CreateFolder.Response(id = folder.id)
    }
}