package ga.injuk.graphfs.application.controller.dto.request

data class UpdateResourceRequest(
    val to: To,
) {
    data class To(
        val id: String,
    )
}
