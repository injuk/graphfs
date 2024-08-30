package ga.injuk.graphfs.application

import kotlinx.coroutines.reactor.awaitSingle
import reactor.core.publisher.Flux

// TODO: 추후에는 mapper를 따로 빼는게 좋을 듯 하다!
object ReactiveExtension {
    suspend fun <T> Flux<T>.toList(): List<T> = collectList().awaitSingle()
}