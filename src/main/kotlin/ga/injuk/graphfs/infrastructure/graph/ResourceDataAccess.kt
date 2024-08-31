package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Resource
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

interface ResourceDataAccess : ReactiveNeo4jRepository<Resource, String> {
}