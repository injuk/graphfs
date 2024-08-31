package ga.injuk.graphfs.domain.useCase.drive

import ga.injuk.graphfs.domain.useCase.UseCase

interface UpdateDrive : UseCase<UpdateDrive.Request, Unit> {
    override val name: String
        get() = UpdateDrive::class.java.name

    data class Request(
        val id: String,
        val name: String,
    )
}