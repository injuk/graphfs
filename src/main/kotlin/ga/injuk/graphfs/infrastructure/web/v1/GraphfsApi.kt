package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.controller.DriveController
import ga.injuk.graphfs.application.controller.FolderController
import ga.injuk.graphfs.application.controller.dto.request.CreateDriveRequest
import ga.injuk.graphfs.application.controller.dto.request.CreateFolderRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateDriveRequest
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.Parent
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RestController
@RequestMapping("/api/v1/drives")
class GraphfsApi(
    private val createDriveUseCase: CreateDrive,
    private val listDrivesUseCase: ListDrives,
    private val getDriveUseCase: GetDrive,
    private val updateDriveUseCase: UpdateDrive,
    private val createFolderUseCase: CreateFolder,
) : DriveController, FolderController {
    companion object {
        private const val LOCATION_PREFIX = "http://localhost:33780/api/v1/drives"
        private fun createSystemUser(): User = User.create()
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun create(
        @RequestBody request: CreateDriveRequest,
    ): ResponseEntity<Unit> {
        val (driveId) = createSystemUser().invoke(createDriveUseCase)
            .with(
                CreateDrive.Request(
                    domain = request.domain,
                    name = request.name,
                )
            )
            .execute()

        return ResponseEntity.created(
            URI.create("$LOCATION_PREFIX/$driveId")
        )
            .build()
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun list(
        @RequestParam domain: String?,
    ): ResponseEntity<ListResponse<Drive>> {
        val drives = createSystemUser().invoke(listDrivesUseCase)
            .with(
                ListDrives.Request(
                    domain = domain,
                )
            )
            .execute()

        return ResponseEntity.ok(
            ListResponse(drives),
        )
    }

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun get(
        @PathVariable id: String,
    ): ResponseEntity<Drive> {
        val drive = createSystemUser().invoke(getDriveUseCase)
            .with(
                GetDrive.Request(
                    id = id,
                )
            )
            .execute()

        return ResponseEntity.ok(
            drive,
        )
    }

    @PatchMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun update(
        @PathVariable id: String,
        @RequestBody request: UpdateDriveRequest,
    ): ResponseEntity<Unit> {
        createSystemUser().invoke(updateDriveUseCase)
            .with(
                UpdateDrive.Request(
                    id = id,
                    name = request.name,
                )
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }

    @PostMapping(
        value = ["/{driveId}/folders"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun create(
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