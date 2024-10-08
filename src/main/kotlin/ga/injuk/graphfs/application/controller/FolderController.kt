package ga.injuk.graphfs.application.controller

import ga.injuk.graphfs.application.controller.dto.request.CreateFolderRequest
import ga.injuk.graphfs.application.controller.dto.request.UpdateFolderRequest
import ga.injuk.graphfs.application.controller.dto.response.GetFolderResponse
import ga.injuk.graphfs.application.controller.dto.response.ListResponse
import ga.injuk.graphfs.domain.Folder
import org.springframework.http.ResponseEntity

interface FolderController : Controller {
    suspend fun create(driveId: String, request: CreateFolderRequest): ResponseEntity<Unit>
    suspend fun list(driveId: String, name: String?): ResponseEntity<ListResponse<Folder>>
    suspend fun get(driveId: String, id: String): ResponseEntity<GetFolderResponse>
    suspend fun listElders(driveId: String, id: String): ResponseEntity<ListResponse<Folder>>
    suspend fun update(driveId: String, id: String, request: UpdateFolderRequest): ResponseEntity<Unit>
    suspend fun delete(driveId: String, id: String): ResponseEntity<Unit>
}