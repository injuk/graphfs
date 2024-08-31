package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.CreateDriveRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateDriveRequest
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Drive
import org.springframework.http.ResponseEntity

interface DriveController : Controller {
    suspend fun create(request: CreateDriveRequest): ResponseEntity<Unit>
    suspend fun list(domain: String?): ResponseEntity<ListResponse<Drive>>
    suspend fun get(id: String): ResponseEntity<Drive>
    suspend fun update(id: String, request: UpdateDriveRequest): ResponseEntity<Unit>
    suspend fun delete(id: String): ResponseEntity<Unit>
}