package ga.injuk.graphfs.application.service.command

import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.infrastructure.graph.Neo4jDataAccess
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class Service(
    private val repository: Neo4jDataAccess,
) {
    suspend fun save(folder: Folder): Folder {
        return repository.save(folder)
            .awaitSingle()
    }

    suspend fun getFolderByName(name: String): Folder? {
        return repository.findOneByName(name)
            .awaitSingle()
    }

    suspend fun deleteById(id: String) {
        repository.deleteById(id).awaitSingleOrNull()
    }
}