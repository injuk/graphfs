package ga.injuk.graphfs.domain.useCase

import ga.injuk.graphfs.domain.Drive

interface ListDrives : UseCase<ListDrives.Request, List<Drive>> {
    override val name: String
        get() = ListDrives::class.java.name

    data class Request(
        val domain: String?,
    )
}