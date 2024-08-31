package ga.injuk.graphfs.application.controller

import org.springframework.http.ResponseEntity

interface ResourceController : Controller {
    suspend fun create(driveId: String, folderId: String): ResponseEntity<Unit>
    suspend fun delete(driveId: String, folderId: String, id: String): ResponseEntity<Unit>
}