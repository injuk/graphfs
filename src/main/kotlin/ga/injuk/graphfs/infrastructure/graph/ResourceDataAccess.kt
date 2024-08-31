package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Resource
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ResourceDataAccess : ReactiveNeo4jRepository<Resource, String> {
    @Query("MATCH (resources: Resource)-[:RESOURCE_OF]->(:Folder)<-[:DIRECT_CHILD*0..]-(folder: Folder) WHERE folder.id = \$folderId RETURN resources")
    fun findResourcesOfDescendantsByFolderId(folderId: String): Flux<Resource>

    @Query("MATCH (resource: Resource)-[:RESOURCE_OF]->(folder: Folder) WHERE resource.id = \$id AND folder.id = \$folderId RETURN resource")
    fun findByFolderIdAndId(folderId: String, id: String): Mono<Resource>

    @Query(
        "MATCH (resource: Resource)-[oldRelation:RESOURCE_OF]->(:Folder) " +
                "MATCH (newFolder: Folder) " +
                "WHERE resource.id = \$id AND newFolder.id = \$newFolderId " +
                "CREATE (resource)-[:RESOURCE_OF]->(newFolder) " +
                "DELETE oldRelation " +
                "RETURN resource"
    )
    fun moveFolder(id: String, newFolderId: String): Mono<Resource>
}