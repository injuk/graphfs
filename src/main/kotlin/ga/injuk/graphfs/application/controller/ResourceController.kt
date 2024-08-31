package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.UpdateResourceRequest
import org.springframework.http.ResponseEntity

interface ResourceController : Controller {
    suspend fun create(driveId: String, folderId: String): ResponseEntity<Unit>
    suspend fun update(
        driveId: String,
        folderId: String,
        id: String,
        request: UpdateResourceRequest,
    ): ResponseEntity<Unit>

    suspend fun delete(driveId: String, folderId: String, id: String): ResponseEntity<Unit>
}