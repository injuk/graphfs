package ga.injuk.graphfs.domain

import ga.injuk.graphfs.domain.useCase.UseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class User(
    val id: String,
    val project: Project,
) {
    companion object {
        fun create(): User = User(
            id = "System",
            project = Project(
                id = "project",
                name = "Project for test",
            ),
        )
    }

    private val traceId: String by lazy { UUID.randomUUID().toString() }

    fun <Request, Response> invoke(useCase: UseCase<Request, Response>): UseCaseHolder<Request, Response> =
        UseCaseHolder(
            user = this,
            useCase = useCase,
        )

    class UseCaseHolder<Request, Response>(
        private val user: User,
        private val useCase: UseCase<Request, Response>,
    ) {
        private var request: Request? = null
        private val logger: Logger = LoggerFactory.getLogger(UseCase::class.java)

        fun with(data: Request): UseCaseHolder<Request, Response> = this.apply { request = data }

        suspend fun execute(): Response {
            checkNotNull(request) { "user request cannot be null" }

            logger.info("user(${user.traceId}) executes useCase(${useCase.name})")

            val result = useCase.execute(user, request!!)

            logger.info("user(${user.traceId}) ends useCase(${useCase.name})")

            return result
        }
    }
}