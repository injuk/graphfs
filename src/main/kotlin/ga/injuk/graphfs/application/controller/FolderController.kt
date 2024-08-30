package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.CreateFolderRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateFolderRequest
import ga.injuk.graphfs.domain.Folder
import org.springframework.http.ResponseEntity

interface FolderController {
    suspend fun create(driveId: String, request: CreateFolderRequest): ResponseEntity<Unit>
    suspend fun get(driveId: String, id: String): ResponseEntity<Folder>
    suspend fun update(driveId: String, id: String, request: UpdateFolderRequest): ResponseEntity<Unit>
    suspend fun delete(driveId: String, id: String): ResponseEntity<Unit>
}