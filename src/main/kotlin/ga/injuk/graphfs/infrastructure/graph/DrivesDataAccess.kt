package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Drive
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Mono

interface DrivesDataAccess : ReactiveNeo4jRepository<Drive, String> {
    @Query("MATCH (d: Drive) WHERE d.projectId = \$projectId AND d.id = \$driveId RETURN d")
    fun findByProjectIdAndId(projectId: String, driveId: String): Mono<Drive>
}