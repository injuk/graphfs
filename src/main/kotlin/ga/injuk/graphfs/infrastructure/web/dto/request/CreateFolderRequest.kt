package ga.injuk.graphfs.infrastructure.web.dto.request

import ga.injuk.graphfs.domain.Parent

data class CreateFolderRequest(
    val name: String,
    val parent: Parent?,
)