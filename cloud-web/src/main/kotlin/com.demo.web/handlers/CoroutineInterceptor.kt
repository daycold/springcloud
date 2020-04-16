package com.demo.web.handlers

import com.demo.web.CoroutineResponse
import io.undertow.server.HttpServerExchange
import java.lang.reflect.Method

/**
 * @author Stefan Liu
 */
interface CoroutineInterceptor {
    fun isSupported(target: Any, method: Method): Boolean

    fun preHandle(exchange: HttpServerExchange, target: Any, method: Method)

    /**
     * would use the response of this method as a new response
     */
    fun postHandle(exchange: HttpServerExchange, target: Any, method: Method, response: CoroutineResponse): Any?
}