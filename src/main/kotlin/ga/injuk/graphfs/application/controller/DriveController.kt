package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.infrastructure.web.dto.request.CreateDriveRequest
import org.springframework.http.ResponseEntity

interface DriveController {
    suspend fun create(request: CreateDriveRequest): ResponseEntity<Unit>
}