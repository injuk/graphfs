package ga.injuk.graphfs.application.controller.dto.response

import ga.injuk.graphfs.domain.Folder
import java.time.OffsetDateTime

data class GetFolderResponse(
    val id: String,

    val name: String,

    val parentId: String?,

    val depth: Int,

    val ancestors: List<AncestorFolder>,

    val creator: String,

    val createdAt: OffsetDateTime,

    val children: List<Folder>,
) {
    data class AncestorFolder(
        val id: String,
        val name: String,
        val depth: Int,
    )
}
