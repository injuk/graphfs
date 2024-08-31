package ga.injuk.graphfs.application

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// TODO: 추후에는 mapper를 따로 빼는게 좋을 듯 하다!
object ReactiveExtension {
    suspend fun <T> Flux<T>.toList(): List<T> = collectList().awaitSingle()

    suspend fun <T> Mono<T>.await(): T? = awaitSingleOrNull()
}