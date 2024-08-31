package ga.injuk.graphfs.domain.useCase.drive

import ga.injuk.graphfs.domain.useCase.UseCase

interface DeleteDrive : UseCase<DeleteDrive.Request, Unit> {
    override val name: String
        get() = DeleteDrive::class.java.name

    data class Request(
        val id: String,
    )
}