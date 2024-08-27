package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Drive
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DriveDataAccess : ReactiveNeo4jRepository<Drive, String> {
    @Query("MATCH (drive: Drive) WHERE drive.projectId = \$projectId AND drive.id = \$id RETURN drive")
    fun findByProjectIdAndId(projectId: String, id: String): Mono<Drive>

    @Query("MATCH (drives: Drive) WHERE drives.projectId = \$projectId RETURN drives")
    fun findByProjectId(projectId: String): Flux<Drive>

    @Query("MATCH (drives: Drive) WHERE drives.projectId = \$projectId AND drives.domain = \$domain RETURN drives")
    fun findByProjectIdAndDomain(projectId: String, domain: String): Flux<Drive>
}