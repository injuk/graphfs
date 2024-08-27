package ga.injuk.graphfs.domain.useCase

interface UpdateDrive : UseCase<UpdateDrive.Request, Unit> {
    override val name: String
        get() = UpdateDrive::class.java.name

    data class Request(
        val id: String,
        val name: String,
    )
}