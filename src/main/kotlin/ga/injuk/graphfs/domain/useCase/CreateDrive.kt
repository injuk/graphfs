package ga.injuk.graphfs.domain.useCase

interface CreateDrive : UseCase<CreateDrive.Request, CreateDrive.Response> {
    override val name: String
        get() = CreateDrive::class.java.name

    data class Request(
        val domain: String,
        val name: String,
    )

    data class Response(
        val id: String,
    )
}