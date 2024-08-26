package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.OffsetDateTime
import java.util.*

@Node
data class Folder(
    @Id
    val id: String,

    val name: String,

    val depth: Int,

    val creator: String,

    val createdAt: OffsetDateTime,

    @Relationship(type = "DIRECT_CHILD", direction = Relationship.Direction.OUTGOING)
    val children: List<Folder>,

    @Relationship(type = "HAS_RESOURCES", direction = Relationship.Direction.OUTGOING)
    val resources: List<Resource>,
) {
    companion object {
        private const val DEPTH_OF_ROOT = 1

        fun from(request: Request): Folder = Folder(
            id = UUID.randomUUID().toString(),
            name = request.name,
            depth = request.parent?.depth?.let { it + 1 } ?: DEPTH_OF_ROOT,
            creator = request.createdBy.id,
            createdAt = OffsetDateTime.now(),
            children = emptyList(),
            resources = emptyList(),
        )
    }

    init {
        check(depth > 0) { "folder depth must be positive value" }
    }

    data class Request(
        val name: String,
        val parent: Folder?,
        val createdBy: User,
    )
}