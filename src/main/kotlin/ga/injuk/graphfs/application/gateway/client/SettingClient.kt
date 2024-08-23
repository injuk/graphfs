package ga.injuk.graphfs.application.gateway.client

import ga.injuk.graphfs.domain.Project
import ga.injuk.graphfs.domain.RootFolder

interface SettingClient {
    suspend fun getRootFolder(domain: String, project: Project): RootFolder?
}