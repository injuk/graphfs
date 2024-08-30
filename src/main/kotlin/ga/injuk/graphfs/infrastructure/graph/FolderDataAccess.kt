package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Folder
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FolderDataAccess : ReactiveNeo4jRepository<Folder, String> {
    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD*]->(folder: Folder) WHERE drive.id = \$driveId AND folder.id = \$id RETURN folder LIMIT 1")
    fun findByDriveAndId(driveId: String, id: String): Mono<Folder>

    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD]->(roots: Folder) WHERE drive.projectId = \$projectId AND drive.id = \$driveId RETURN roots")
    fun findRootsByProjectIdAndDriveId(projectId: String, driveId: String): Flux<Folder>

    @Query("MATCH (folder: Folder)-[:DIRECT_CHILD]->(children: Folder) WHERE folder.id = \$id RETURN children")
    fun findChildrenById(id: String): Flux<Folder>
}