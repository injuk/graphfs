package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Resource(
    @Id
    val id: String,

    @Relationship(type = "RESOURCE_OF", direction = Relationship.Direction.OUTGOING)
    val folder: Folder,
)
