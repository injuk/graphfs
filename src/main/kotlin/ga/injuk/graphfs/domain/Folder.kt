package ga.injuk.graphfs.domain

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import java.time.OffsetDateTime

@Node
data class Folder(
    @Id
    val id: String,

    val name: String,

    val depth: Int,

    val creator: String,

    val createdAt: OffsetDateTime,
) {
    init {
        check(depth >= 0) { "folder depth cannot be negative" }
    }
}