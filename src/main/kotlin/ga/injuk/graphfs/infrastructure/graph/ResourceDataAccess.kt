package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Resource
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import reactor.core.publisher.Flux

interface ResourceDataAccess : ReactiveNeo4jRepository<Resource, String> {
    @Query("MATCH (resources: Resource)-[:RESOURCE_OF]->(:Folder)<-[:DIRECT_CHILD*0..]-(folder: Folder) WHERE folder.id = \$folderId RETURN resources")
    fun findResourcesOfDescendantsByFolderId(folderId: String): Flux<Resource>
}