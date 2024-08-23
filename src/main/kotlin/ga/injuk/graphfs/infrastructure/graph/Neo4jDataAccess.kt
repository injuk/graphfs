package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Folder
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Mono

interface Neo4jDataAccess : ReactiveNeo4jRepository<Folder, String> {
    @Query("MATCH (root: Folder)-[:DIRECT_CHILDREN*]->(f: Folder) WHERE root.id = \$rootId AND f.id = \$id RETURN f LIMIT 1")
    fun findOneById(rootId: String, id: String): Mono<Folder?>
}