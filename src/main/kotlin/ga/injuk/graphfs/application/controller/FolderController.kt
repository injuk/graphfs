package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.infrastructure.web.dto.request.CreateFolderRequest
import org.springframework.http.ResponseEntity

interface FolderController {
    suspend fun create(driveId: String, request: CreateFolderRequest): ResponseEntity<Unit>
}