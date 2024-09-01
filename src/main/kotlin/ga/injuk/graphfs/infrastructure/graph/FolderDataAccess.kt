package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Folder
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface FolderDataAccess : ReactiveNeo4jRepository<Folder, String> {
    @Query("MATCH (drive: Drive)-[:DIRECT_CHILD*]->(folder: Folder) WHERE drive.id = \$driveId AND folder.id = \$id RETURN folder LIMIT 1")
    fun findByDriveIdAndId(driveId: String, id: String): Mono<Folder>

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

    @Query(
        "CALL { MATCH (ancestors: Folder)-[:DIRECT_CHILD*]->(folder: Folder) WHERE folder.id = \$id " +
                "MATCH (ancestors)-[:DIRECT_CHILD]->(elders: Folder) RETURN elders " +
                "UNION " +
                "MATCH (drive :Drive)-[:DIRECT_CHILD]->(elders: Folder) WHERE drive.id = \$driveId " +
                "RETURN elders } RETURN elders ORDER BY elders.depth"
    )
    fun findEldersByDriveIdAndId(driveId: String, id: String): Flux<Folder>

    @Query("MATCH (folder: Folder)-[:DIRECT_CHILD*0..]->(descendants: Folder) WHERE folder.id = \$id DETACH DELETE descendants")
    fun deleteAllDescendantsById(id: String): Mono<Void>

    @Query(
        "MATCH (drive: Drive) WHERE drive.id = \$driveId " +
                "MATCH ()-[oldRelation:DIRECT_CHILD]->(targetFolder: Folder) WHERE targetFolder.id = \$id " +
                "CREATE (drive)-[:DIRECT_CHILD]->(targetFolder) " +
                "SET targetFolder.parentId = null " +
                "DELETE oldRelation " +
                "WITH targetFolder, 1 - targetFolder.depth as increment " +
                "MATCH (targetFolder)-[:DIRECT_CHILD*0..]->(affectedFolders: Folder) " +
                "SET affectedFolders.depth = affectedFolders.depth + increment"
    )
    fun updateParentToDriveByDriveIdAndId(driveId: String, id: String): Mono<Void>

    @Query(
        "MATCH ()-[oldRelation:DIRECT_CHILD]->(targetFolder: Folder) WHERE targetFolder.id = \$targetId " +
                "MATCH (newParent: Folder) WHERE newParent.id = \$newParentId AND NOT (targetFolder)-[:DIRECT_CHILD*0..]->(newParent) " +
                "CREATE (newParent)-[:DIRECT_CHILD]->(targetFolder) " +
                "SET targetFolder.parentId = \$newParentId " +
                "DELETE oldRelation " +
                "WITH targetFolder, newParent.depth - targetFolder.depth + 1 as increment " +
                "MATCH (targetFolder)-[:DIRECT_CHILD*0..]->(affectedFolders: Folder) " +
                "SET affectedFolders.depth = affectedFolders.depth + increment"
    )
    fun updateParentById(targetId: String, newParentId: String): Mono<Void>
}