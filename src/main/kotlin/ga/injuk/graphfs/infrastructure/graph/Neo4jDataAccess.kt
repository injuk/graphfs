package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Folder
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import reactor.core.publisher.Mono

interface Neo4jDataAccess : ReactiveNeo4jRepository<Folder, String> {
    fun findOneByName(name: String): Mono<Folder?>
}