package ga.injuk.graphfs.infrastructure.setting

import ga.injuk.graphfs.application.gateway.client.SettingClient
import ga.injuk.graphfs.domain.Constants
import ga.injuk.graphfs.domain.Folder
import ga.injuk.graphfs.domain.Project
import ga.injuk.graphfs.domain.RootFolder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class InMemoryClient : SettingClient {
    companion object {
        private val settings: MutableMap<CacheKey, RootFolder> = mutableMapOf()
        private val logger = LoggerFactory.getLogger(InMemoryClient::class.java)
    }

    override suspend fun getRootFolder(domain: String, project: Project): RootFolder? {
        val cacheKey = CacheKey(domain, project)

        return settings[cacheKey].let { cachedFolder ->
            if (cachedFolder == null) {
                val rootFolder = getFromSomewhere(domain, project)
                settings[cacheKey] = rootFolder

                rootFolder
            } else {
                cachedFolder
            }
        }
    }

    private fun getFromSomewhere(domain: String, project: Project): RootFolder {
        logger.info("there is no cached root folder... get new one from somewhere")

        return RootFolder(
            info = Folder(
                id = Constants.ROOT_FOLDER_ID,
                name = Constants.ROOT_FOLDER_NAME,
                depth = 0,
                creator = Constants.SYSTEM,
                createdAt = OffsetDateTime.now(),
            ),
            project = project.copy(),
            domain = domain,
        )
    }

    private data class CacheKey(
        val domain: String,
        val project: Project,
    )
}