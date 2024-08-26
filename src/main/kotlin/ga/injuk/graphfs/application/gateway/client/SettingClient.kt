package ga.injuk.graphfs.application.gateway.client

import ga.injuk.graphfs.domain.Drive
import ga.injuk.graphfs.domain.Project

interface SettingClient {
    suspend fun getDriveInfo(project: Project, driveId: String): Drive
}