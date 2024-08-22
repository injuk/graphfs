package ga.injuk.graphfs.domain

data class RootFolder(
    val info: Folder,
    val project: Project,
    val domain: String,
) {
    init {
        check(info.depth == 0) { "depth of root folder must be 0" }
    }
}