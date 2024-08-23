package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
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
) {
    companion object {
        fun from(request: Request): Folder {
            return Folder(
                id = UUID.randomUUID().toString(),
                name = request.name,
                depth = request.parent.depth + 1,
                creator = request.createdBy.id,
                createdAt = OffsetDateTime.now(),
            )
        }
    }

    init {
        check(depth >= 0) { "folder depth cannot be negative" }
    }

    data class Request(
        val name: String,
        val parent: Folder,
        val createdBy: User,
    )
}