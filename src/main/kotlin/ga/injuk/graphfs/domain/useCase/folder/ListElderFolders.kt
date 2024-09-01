package ga.injuk.graphfs.domain.useCase.folder

import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.useCase.UseCase

/**
 * 파일시스템에서 1과 2이라는 최상위 폴더가 있고, 1은 자식 폴더 1-1, 1-2, 1-3 / 2는 자식 폴더 2-1을 갖는다고 하자.
 * 또한, 1-2는 다시 자식 폴더인 1-2-1과 1-2-2를 갖고 2-1은 2-1-1, 2-1-2, 2-1-3을 갖는다.
 * 이러한 구조를 트리 형태로 보여주는 UI가 있다고 할 때, 사용자가 1-2-1의 정보를 요청한다면
 * 1-2-1까지의 경로가 마치 열려 있는 것처럼 보여주기 위해 1, 2, 1-1, 1-2, 1-3, 1-2-1, 1-2-2를 반환할 API가 필요하다.
 * 즉, ListElderFolders 유즈케이스는 임의의 폴더로부터 최상위 조상 폴더까지의 경로 상에 포함된 모든 폴더를 반환한다.
 */
interface ListElderFolders : UseCase<ListElderFolders.Request, List<Folder>> {
    override val name: String
        get() = ListElderFolders::class.java.name

    data class Request(
        val driveId: String,
        val id: String,
    )
}