package ga.injuk.graphfs.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "server")
data class GraphfsProperty(
    val port: Int,
    val address: String,
)
