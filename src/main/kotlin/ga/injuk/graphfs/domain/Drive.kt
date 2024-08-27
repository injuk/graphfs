package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.OffsetDateTime
import java.util.*

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
) {
    companion object {
        fun from(request: Request): Drive = Drive(
            id = UUID.randomUUID().toString(),
            projectId = request.createdBy.project.id,
            domain = request.domain,
            name = request.name,
            creator = request.createdBy.id,
            createdAt = OffsetDateTime.now(),
            children = emptyList(),
        )
    }

    data class Request(
        val name: String,
        val domain: String,
        val createdBy: User,
    )
}