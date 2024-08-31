package ga.injuk.graphfs.domain.useCase.drive

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.useCase.UseCase

interface ListDrives : UseCase<ListDrives.Request, List<Drive>> {
    override val name: String
        get() = ListDrives::class.java.name

    data class Request(
        val domain: String?,
    )
}