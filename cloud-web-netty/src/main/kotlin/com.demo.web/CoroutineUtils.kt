package com.demo.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.future.future
import kotlinx.coroutines.newFixedThreadPoolContext
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author Stefan Liu
 */
object CoroutineUtils {
    @JvmField
    val DEFAULT_CONTEXT = newFixedThreadPoolContext(16, "default")

    @JvmStatic
    val defaultScope: CoroutineScope
        get() = CoroutineScope(DEFAULT_CONTEXT)
}

private fun <T> CompletableFuture<T>.mono(): Mono<T> = Mono.fromFuture(this)

fun <T> CoroutineScope.mono(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Mono<T> = future(context, start, block).mono()
