package com.demo.web.handlers

import com.demo.web.CoroutineResponse
import com.demo.web.CoroutineUtils
import com.demo.web.io.ByteBufferBackendOutputStream
import com.demo.web.parsers.ParameterData
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

/**
 * @author Stefan Liu
 */
class CoroutineFunctionHandler(
    private val function: KFunction<*>,
    private val obj: Any,
    interceptors: List<CoroutineInterceptor> = listOf()
) : HttpHandler {
    private val method = function.javaMethod!!
    private val defaultStatus: Int
    private val params: List<ParameterData<*>>
    private val supportedInterceptors: List<CoroutineInterceptor>

    init {
        val status = function.findAnnotation<ResponseStatus>()
        defaultStatus = status?.code?.value() ?: 200
        supportedInterceptors = interceptors.filter { it.isSupported(obj, method) }
        val list = mutableListOf<ParameterData<*>>()
        function.parameters.forEach { parameter ->
            val annotations = parameter.annotations
            repeat(annotations.size) { index ->
                val annotation = annotations[index]
                when (annotation) {
                    is RequestParam -> {
                       list.add(ParameterData(annotation.name, parameter.javaClass, ))
                    }
                    is PathVariable -> {
                        name = annotation.name
                        return@repeat
                    }
                    is RequestBody -> {
                        name = ""
                        return@repeat
                    }
                    else -> throw IllegalArgumentException("unsupported arguments")
                }
            }
        }
    }

    override fun handleRequest(exchange: HttpServerExchange) {
        val parameters = getParameter(exchange)
        exchange.dispatch(CoroutineUtils.inPlaceExecutor, Runnable {
            CoroutineScope(CoroutineUtils.COROUTINE_CONTEXT).launch {
                val response = try {
                    supportedInterceptors.forEach { interceptor ->
                        interceptor.preHandle(exchange, obj, method)
                    }
                    if (function.isSuspend)
                        function.callSuspend(obj, *parameters)
                    else
                        function.call(obj, *parameters)

                } catch (e: Throwable) {
                    e
                }
                val res = if (response is CoroutineResponse) response
                else DefaultCoroutineResponse(defaultStatus, response)

                supportedInterceptors.forEach { interceptor ->
                    interceptor.postHandle(exchange, obj, method, res)
                }
                handleResponse(exchange, res)
            }
        })
    }

    private fun getParameter(exchange: HttpServerExchange): Array<Any> {
        exchange.pathParameters
        exchange.queryParameters
        return arrayOf()
    }

    private fun handleResponse(exchange: HttpServerExchange, response: CoroutineResponse) {
        exchange.apply {
            statusCode = response.httpStatus
            responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
            val body = response.body
            if (body != null) {
                ByteBufferBackendOutputStream(connection.byteBufferPool).use { outputStream ->
                    // use interface
                    outputStream.write(body.toString().toByteArray())
                    val buffers = outputStream.byteBuffers
                    buffers.forEach { it.flip() }
                    responseSender.send(outputStream.byteBuffers)
                }
            }
        }
    }

    private class DefaultCoroutineResponse(override val httpStatus: Int, override val body: Any?) : CoroutineResponse
}