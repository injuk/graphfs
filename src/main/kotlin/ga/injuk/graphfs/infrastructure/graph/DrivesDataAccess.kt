package ga.injuk.graphfs.infrastructure.graph

import ga.injuk.graphfs.domain.Drive
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

interface DrivesDataAccess : ReactiveNeo4jRepository<Drive, String> {
}