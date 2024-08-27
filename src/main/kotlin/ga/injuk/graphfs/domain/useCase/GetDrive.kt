package ga.injuk.graphfs.domain.useCase

import ga.injuk.graphfs.domain.Drive

interface GetDrive : UseCase<GetDrive.Request, Drive> {
    override val name: String
        get() = GetDrive::class.java.name

    data class Request(
        val id: String,
    )
}