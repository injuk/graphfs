package ga.injuk.graphfs.infrastructure.http

import ga.injuk.graphfs.application.service.command.Service
import ga.injuk.graphfs.domain.Folder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/folders")
class RestApi(
    private val service: Service,
) {
    @PostMapping
    suspend fun create(@RequestBody folder: Folder) = ResponseEntity.ok(service.save(folder))

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/{name}"],
        consumes = ["application/json"],
    )
    suspend fun getByName(@PathVariable name: String) = ResponseEntity.ok(service.getFolderByName(name))

    @RequestMapping(
        method = [RequestMethod.DELETE],
        value = ["/{id}"],
        consumes = ["application/json"],
    )
    suspend fun deleteByName(@PathVariable id: String): ResponseEntity<Unit> {
        service.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}