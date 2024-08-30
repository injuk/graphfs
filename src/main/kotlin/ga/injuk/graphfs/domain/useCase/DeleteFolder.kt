package ga.injuk.graphfs.domain.useCase

interface DeleteFolder : UseCase<DeleteFolder.Request, Unit> {
    override val name: String
        get() = DeleteFolder::class.java.name

    data class Request(
        val id: String,
        val driveId: String,
    )
}