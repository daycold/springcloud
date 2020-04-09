package com.demo.web

import kotlinx.coroutines.newFixedThreadPoolContext
import kotlin.coroutines.CoroutineContext

/**
 * @author Stefan Liu
 */
object CoroutineUtils {
    @JvmField
    val COROUTINE_CONTEXT: CoroutineContext = newFixedThreadPoolContext(24, "dispatcher")
}