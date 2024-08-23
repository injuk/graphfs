package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.CreateFolderRequest
import org.springframework.http.ResponseEntity

interface Controller {
    suspend fun createFolder(request: CreateFolderRequest): ResponseEntity<Unit>
}