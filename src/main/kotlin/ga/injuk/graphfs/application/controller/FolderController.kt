package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.CreateFolderRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateFolderRequest
import org.springframework.http.ResponseEntity

interface FolderController {
    suspend fun create(driveId: String, request: CreateFolderRequest): ResponseEntity<Unit>
    suspend fun update(driveId: String, id: String, request: UpdateFolderRequest): ResponseEntity<Unit>
}