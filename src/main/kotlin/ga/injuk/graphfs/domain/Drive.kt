package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.OffsetDateTime

@Node
data class Drive(
    @Id
    val id: String,

    val projectId: String,

    val domain: String,

    val name: String,

    val creator: String,

    val createdAt: OffsetDateTime,

    @Relationship(type = "DIRECT_CHILD", direction = Relationship.Direction.OUTGOING)
    val children: List<Folder>,
)
