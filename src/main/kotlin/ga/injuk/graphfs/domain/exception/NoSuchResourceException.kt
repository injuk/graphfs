package ga.injuk.graphfs.domain.exception

class NoSuchResourceException(
    override val message: String,
) : RuntimeException(message)