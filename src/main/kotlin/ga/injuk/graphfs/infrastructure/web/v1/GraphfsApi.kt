package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.controller.Controller
import ga.injuk.graphfs.application.controller.dto.CreateFolderRequest
import ga.injuk.graphfs.domain.Parent
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.CreateFolder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RestController
@RequestMapping("/api/v1/drives")
class GraphfsApi(
    private val createFolderUseCase: CreateFolder,
) : Controller {
    companion object {
        private const val LOCATION_PREFIX = "http://localhost:33780/api/v1/drives"
        private fun createSystemUser(): User = User.create()
    }

    @PostMapping(
        value = ["/{driveId}/folders"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun createFolder(
        @PathVariable("driveId") driveId: String,
        @RequestBody request: CreateFolderRequest,
    ): ResponseEntity<Unit> {
        val (folderId) = createSystemUser().invoke(createFolderUseCase)
            .with(
                CreateFolder.Request(
                    name = request.name,
                    parent = request.parent?.let { Parent(it.id) },
                    driveId = driveId,
                )
            )
            .execute()

        return ResponseEntity.created(
            URI.create("$LOCATION_PREFIX/$driveId/folders/$folderId")
        )
            .build()
    }
}