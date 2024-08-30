package ga.injuk.graphfs.infrastructure.setting

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.Project
import ga.injuk.graphfs.infrastructure.graph.DriveDataAccess
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InMemoryClient(
    private val driveDataAccess: DriveDataAccess,
) : SettingClient {
    companion object {
        private val settings: MutableMap<Project, Drive> = mutableMapOf()
        private val logger = LoggerFactory.getLogger(InMemoryClient::class.java)
    }

    override suspend fun getDriveInfo(project: Project, driveId: String): Drive {
        if (settings.notContainsKey(project)) {
            settings[project] = fetchDrive(project, driveId)
        }

        return settings[project]!!
    }

    private fun MutableMap<Project, Drive>.notContainsKey(key: Project): Boolean = !containsKey(key)

    private suspend fun fetchDrive(project: Project, driveId: String): Drive {
        logger.info("there is no cached drive... try to fetch from neo4j")

        return driveDataAccess.findByProjectIdAndId(project.id, driveId)
            .awaitSingleOrNull() ?: throw RuntimeException("there is no drive($driveId) in project")
    }
}