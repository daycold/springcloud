package com.demo.web

import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.AttachmentKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.concurrent.Executor

/**
 * @author Stefan Liu
 */
class CoroutinePathHandler(private val originHandler: HttpHandler) : HttpHandler {
    override fun handleRequest(exchange: HttpServerExchange) {
        val scope = CoroutineScope(CoroutineUtils.COROUTINE_CONTEXT)
        exchange.putAttachment(ATTACHMENT_KEY, scope)
        exchange.dispatch(inPlaceExecutor, Runnable {
            scope.launch {
                originHandler.handleRequest(exchange)
            }
        })
        originHandler.handleRequest(exchange)
    }

    companion object {
        val ATTACHMENT_KEY = AttachmentKey.create(CoroutineScope::class.java)!!
        val inPlaceExecutor = Executor { it.run() }
    }
}
