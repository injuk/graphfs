package ga.injuk.graphfs.domain.useCase

interface CreateFolder : UseCase<CreateFolder.Request, CreateFolder.Response> {
    override val name: String
        get() = CreateFolder::class.java.name

    data class Request(
        val name: String,
        val depth: Int,
    )

    data class Response(
        val id: String,
    )
}