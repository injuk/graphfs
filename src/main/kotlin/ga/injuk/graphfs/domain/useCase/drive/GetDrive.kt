package ga.injuk.graphfs.domain.useCase.drive

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.useCase.UseCase

interface GetDrive : UseCase<GetDrive.Request, Drive> {
    override val name: String
        get() = GetDrive::class.java.name

    data class Request(
        val id: String,
    )
}