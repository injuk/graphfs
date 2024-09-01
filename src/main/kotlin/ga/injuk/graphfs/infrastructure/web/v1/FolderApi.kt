package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.configuration.Beans
import ga.injuk.graphfs.application.controller.FolderController
import ga.injuk.graphfs.application.controller.dto.request.CreateFolderRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateFolderRequest
import ga.injuk.graphfs.application.controller.dto.response.GetFolderResponse
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.Parent
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.folder.*
import ga.injuk.graphfs.infrastructure.web.WebConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RestController
@RequestMapping(WebConstants.API_PREFIX)
class FolderApi(
    private val createFolderUseCase: CreateFolder,
    private val listFoldersUseCase: ListFolders,
    private val getFolderUseCase: GetFolder,
    private val listElderFoldersUseCase: ListElderFolders,
    private val updateFolderUseCase: UpdateFolder,
    private val deleteFolderUseCase: DeleteFolder,

    private val graphfsProperty: Beans.GraphfsProperty,
) : FolderController {
    override val locationPrefix: String
        get() = "${graphfsProperty.locationPrefix}${WebConstants.API_PREFIX}"

    @PostMapping(
        value = ["/{driveId}/folders"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun create(
        @PathVariable("driveId") driveId: String,
        @RequestBody request: CreateFolderRequest,
    ): ResponseEntity<Unit> {
        val (folderId) = User.create()
            .invoke(createFolderUseCase)
            .with(
                CreateFolder.Request(
                    name = request.name,
                    parent = request.parent?.let { Parent(it.id) },
                    driveId = driveId,
                )
            )
            .execute()

        return ResponseEntity.created(
            URI.create("$locationPrefix/$driveId/folders/$folderId")
        )
            .build()
    }

    @GetMapping(
        value = ["/{driveId}/folders"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun list(
        @PathVariable driveId: String,
        @RequestParam(value = "name") name: String?,
    ): ResponseEntity<ListResponse<Folder>> {
        val folders = User.create()
            .invoke(listFoldersUseCase)
            .with(
                ListFolders.Request(
                    driveId = driveId,
                    name = name,
                ),
            )
            .execute()

        return ResponseEntity.ok(
            ListResponse(
                items = folders,
            )
        )
    }

    @GetMapping(
        value = ["/{driveId}/folders/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun get(
        @PathVariable driveId: String,
        @PathVariable id: String,
    ): ResponseEntity<GetFolderResponse> {
        val (folder, ancestors) = User.create()
            .invoke(getFolderUseCase)
            .with(
                GetFolder.Request(
                    id = id,
                    driveId = driveId,
                ),
            )
            .execute()

        return ResponseEntity.ok(
            GetFolderResponse(
                id = folder.id,
                name = folder.name,
                parentId = folder.name,
                ancestors = ancestors.map {
                    GetFolderResponse.AncestorFolder(
                        id = it.id,
                        name = it.name,
                        depth = it.depth,
                    )
                },
                depth = folder.depth,
                creator = folder.creator,
                createdAt = folder.createdAt,
                children = folder.children,
            )
        )
    }

    @GetMapping(
        value = ["/{driveId}/folders/{id}/elders"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun listElders(
        @PathVariable driveId: String,
        @PathVariable id: String,
    ): ResponseEntity<ListResponse<Folder>> {
        val elders = User.create()
            .invoke(listElderFoldersUseCase)
            .with(
                ListElderFolders.Request(
                    driveId = driveId,
                    id = id,
                ),
            )
            .execute()

        return ResponseEntity.ok(
            ListResponse(
                items = elders,
            )
        )
    }

    @PatchMapping(
        value = ["/{driveId}/folders/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun update(
        @PathVariable driveId: String,
        @PathVariable id: String,
        @RequestBody request: UpdateFolderRequest,
    ): ResponseEntity<Unit> {
        User.create()
            .invoke(updateFolderUseCase)
            .with(
                UpdateFolder.Request(
                    id = id,
                    driveId = driveId,
                    name = request.name,
                )
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }

    @DeleteMapping(
        value = ["/{driveId}/folders/{id}"],
    )
    override suspend fun delete(
        @PathVariable driveId: String,
        @PathVariable id: String,
    ): ResponseEntity<Unit> {
        User.create()
            .invoke(deleteFolderUseCase)
            .with(
                DeleteFolder.Request(
                    id = id,
                    driveId = driveId,
                )
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }
}