package ga.injuk.graphfs.infrastructure.web.v1

import ga.injuk.graphfs.application.configuration.Beans
import ga.injuk.graphfs.application.controller.ResourceController
import ga.injuk.graphfs.domain.User
import ga.injuk.graphfs.domain.useCase.resource.CreateResource
import ga.injuk.graphfs.infrastructure.web.WebConstants
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Validated
@RestController
@RequestMapping(WebConstants.API_PREFIX)
class ResourceApi(
    private val createResourceUseCase: CreateResource,

    private val graphfsProperty: Beans.GraphfsProperty,
) : ResourceController {
    override val locationPrefix: String
        get() = "${graphfsProperty.locationPrefix}${WebConstants.API_PREFIX}"

    @PostMapping(
        value = ["/{driveId}/folders/{folderId}/resources"]
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
}