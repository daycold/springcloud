package com.demo.web.handlers

import com.demo.web.CoroutineResponse
import com.demo.web.CoroutineUtils
import com.demo.web.io.ByteBufferBackendOutputStream
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.handlers.ServletRequestContext
import io.undertow.servlet.spec.HttpServletRequestImpl
import io.undertow.servlet.spec.HttpServletResponseImpl
import io.undertow.servlet.spec.ServletContextImpl
import io.undertow.util.Headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.javaMethod

/**
 * @author Stefan Liu
 */
class InvocationHttpHandler(
    private val function: KFunction<*>,
    private val target: Any,
    resolvers: Collection<HandlerMethodArgumentResolver>,
    private val servletContext: ServletContextImpl
) : HttpHandler {
    private val argumentResolvers: Map<MethodParameter, HandlerMethodArgumentResolver>
    private val methodParameters: List<MethodParameter>

    init {
        val method = function.javaMethod!!
        val arguments = method.parameters
        val size = if (function.isSuspend) {
            arguments.size - 1
        } else {
            arguments.size
        }
        val parameters = mutableListOf<MethodParameter>()
        val resolverMap = mutableMapOf<MethodParameter, HandlerMethodArgumentResolver>()
        repeat(size) { index ->
            val param = MethodParameter(method, index)
            parameters.add(param)
            resolverMap[param] = resolvers.first { it.supportsParameter(param) }
        }
        methodParameters = parameters
        argumentResolvers = resolverMap
    }

    override fun handleRequest(exchange: HttpServerExchange) {
        CoroutineScope(CoroutineUtils.COROUTINE_CONTEXT).launch {
            val request = HttpServletRequestImpl(exchange, servletContext)
            val response = HttpServletResponseImpl(exchange, servletContext)
            val servletWebRequest = ServletWebRequest(request)
            val servletRequestContext = ServletRequestContext(servletContext.deployment, request, response, null)

            exchange.putAttachment(ServletRequestContext.ATTACHMENT_KEY, servletRequestContext)
            val arguments = methodParameters.map { param ->
                argumentResolvers.getValue(param).resolveArgument(param, null, servletWebRequest, null)
            }.toTypedArray()
            val body = if (function.isSuspend) {
                function.callSuspend(target, *arguments)
            } else {
                function.call(target, *arguments)
            }
            exchange.apply {
                val coroutineResponse = DefaultCoroutineResponse(200, body)
                handleResponse(this, coroutineResponse)
                removeAttachment(ServletRequestContext.ATTACHMENT_KEY)
            }
        }
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