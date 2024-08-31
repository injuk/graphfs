package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Folder
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FolderDataAccess : ReactiveNeo4jRepository<Folder, String> {
    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD*]->(folder: Folder) WHERE drive.id = \$driveId AND folder.id = \$id RETURN folder LIMIT 1")
    fun findByDriveAndId(driveId: String, id: String): Mono<Folder>

    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD*]->(folders: Folder) WHERE drive.id = \$driveId RETURN folders ORDER BY folders.depth")
    fun findAllByDriveId(driveId: String): Flux<Folder>

    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD*]->(folders: Folder) WHERE drive.id = \$driveId AND folders.name STARTS WITH \$name RETURN folders ORDER BY folders.depth")
    fun findAllByDriveIdAndName(driveId: String, name: String): Flux<Folder>

    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD]->(roots: Folder) WHERE drive.projectId = \$projectId AND drive.id = \$driveId RETURN roots")
    fun findRootsByProjectIdAndDriveId(projectId: String, driveId: String): Flux<Folder>

    @Query("MATCH (folder: Folder)-[:DIRECT_CHILD]->(children: Folder) WHERE folder.id = \$id RETURN children")
    fun findChildrenById(id: String): Flux<Folder>

    @Query("MATCH (ancestors: Folder)-[:DIRECT_CHILD*]->(folder: Folder) WHERE folder.id = \$id RETURN ancestors ORDER BY ancestors.depth")
    fun findAncestorsById(id: String): Flux<Folder>

    @Query("MATCH (folder: Folder)-[:DIRECT_CHILD*0..]->(descendants: Folder) WHERE folder.id = \$id DETACH DELETE descendants")
    fun deleteAllDescendantsById(id: String): Mono<Void>
}