package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.CreateDriveRequest
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Drive
import org.springframework.http.ResponseEntity

interface DriveController {
    suspend fun create(request: CreateDriveRequest): ResponseEntity<Unit>
    suspend fun list(domain: String?): ResponseEntity<ListResponse<Drive>>
}