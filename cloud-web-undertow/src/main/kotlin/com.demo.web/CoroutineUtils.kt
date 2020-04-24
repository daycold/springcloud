package com.demo.web

import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * @author Stefan Liu
 */
object CoroutineUtils {
    @JvmField
    val COROUTINE_CONTEXT: CoroutineContext = newFixedThreadPoolContext(24, "dispatcher")

    @JvmField
    val inPlaceExecutor = Executor { it.run() }
}