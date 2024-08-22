package ga.injuk.graphfs.application.configuration

import org.neo4j.cypherdsl.core.renderer.Dialect
import org.neo4j.driver.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import org.neo4j.cypherdsl.core.renderer.Configuration as Neo4JConfiguration

@Configuration
class Beans {

    @Bean
    fun cypherDslConfiguration(): Neo4JConfiguration = Neo4JConfiguration.newConfig().run {
        withDialect(Dialect.NEO4J_5)
        build()
    }

    @Bean
    fun reactiveTransactionManager(
        driver: Driver,
        databaseNameProvider: ReactiveDatabaseSelectionProvider,
    ): ReactiveNeo4jTransactionManager {
        return ReactiveNeo4jTransactionManager(driver, databaseNameProvider)
    }
}