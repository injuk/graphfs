package ga.injuk.graphfs.application.controller.dto

import ga.injuk.graphfs.domain.Parent

data class CreateFolderRequest(
    val domain: String,
    val name: String,
    val parent: Parent,
)