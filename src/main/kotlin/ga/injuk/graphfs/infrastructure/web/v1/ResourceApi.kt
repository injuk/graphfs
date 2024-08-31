package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.configuration.Beans
import ga.injuk.graphfs.application.controller.ResourceController
import ga.injuk.graphfs.application.controller.dto.request.UpdateResourceRequest
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.resource.CreateResource
import ga.injuk.graphfs.domain.useCase.resource.DeleteResource
import ga.injuk.graphfs.domain.useCase.resource.UpdateResource
import ga.injuk.graphfs.infrastructure.web.WebConstants
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RestController
@RequestMapping(WebConstants.API_PREFIX)
class ResourceApi(
    private val createResourceUseCase: CreateResource,
    private val updateResourceUseCase: UpdateResource,
    private val deleteResourceUseCase: DeleteResource,

    private val graphfsProperty: Beans.GraphfsProperty,
) : ResourceController {
    override val locationPrefix: String
        get() = "${graphfsProperty.locationPrefix}${WebConstants.API_PREFIX}"

    @PostMapping(
        value = ["/{driveId}/folders/{folderId}/resources"],
    )
    override suspend fun create(
        @PathVariable driveId: String,
        @PathVariable folderId: String,
    ): ResponseEntity<Unit> {
        val (resourceId) = User.create()
            .invoke(createResourceUseCase)
            .with(
                CreateResource.Request(
                    driveId = driveId,
                    folderId = folderId,
                )
            )
            .execute()

        return ResponseEntity.created(
            URI.create("$locationPrefix/$driveId/folders/$folderId/resources/$resourceId")
        )
            .build()
    }

    @DeleteMapping(
        value = ["/{driveId}/folders/{folderId}/resources/{id}"],
    )
    override suspend fun delete(
        @PathVariable driveId: String,
        @PathVariable folderId: String,
        @PathVariable id: String,
    ): ResponseEntity<Unit> {
        User.create()
            .invoke(deleteResourceUseCase)
            .with(
                DeleteResource.Request(
                    driveId = driveId,
                    folderId = folderId,
                    id = id,
                ),
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }

    @PatchMapping(
        value = ["/{driveId}/folders/{folderId}/resources/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    override suspend fun update(
        @PathVariable driveId: String,
        @PathVariable folderId: String,
        @PathVariable id: String,
        @RequestBody request: UpdateResourceRequest,
    ): ResponseEntity<Unit> {
        User.create()
            .invoke(updateResourceUseCase)
            .with(
                UpdateResource.Request(
                    driveId = driveId,
                    folderId = folderId,
                    id = id,
                    newFolderId = request.to.id,
                ),
            )
            .execute()

        return ResponseEntity.noContent()
            .build()
    }
}