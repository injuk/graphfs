package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.configuration.Beans
import ga.injuk.graphfs.application.controller.DriveController
import ga.injuk.graphfs.application.controller.dto.request.CreateDriveRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateDriveRequest
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.drive.*
import ga.injuk.graphfs.infrastructure.web.WebConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RestController
@RequestMapping(WebConstants.API_PREFIX)
class DriveApi(
    private val createDriveUseCase: CreateDrive,
    private val listDrivesUseCase: ListDrives,
    private val getDriveUseCase: GetDrive,
    private val updateDriveUseCase: UpdateDrive,
    private val deleteDriveUseCase: DeleteDrive,

    private val graphfsProperty: Beans.GraphfsProperty,
) : DriveController {
    override val locationPrefix: String
        get() = "${graphfsProperty.locationPrefix}${WebConstants.API_PREFIX}"

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun create(
        @RequestBody request: CreateDriveRequest,
    ): ResponseEntity<Unit> {
        val (driveId) = User.create()
            .invoke(createDriveUseCase)
            .with(
                CreateDrive.Request(
                    domain = request.domain,
                    name = request.name,
                )
            )
            .execute()

        return ResponseEntity.created(
            URI.create("$locationPrefix/$driveId")
        )
            .build()
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun list(
        @RequestParam domain: String?,
    ): ResponseEntity<ListResponse<Drive>> {
        val drives = User.create()
            .invoke(listDrivesUseCase)
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
        val drive = User.create()
            .invoke(getDriveUseCase)
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
        User.create()
            .invoke(updateDriveUseCase)
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

    @DeleteMapping(
        value = ["/{id}"],
    )
    override suspend fun delete(
        @PathVariable id: String,
    ): ResponseEntity<Unit> {
        User.create()
            .invoke(deleteDriveUseCase)
            .with(
                DeleteDrive.Request(
                    id = id,
                )
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }
}