package com.demo.web.handlers

import com.demo.web.CoroutineResponse
import com.demo.web.CoroutineUtils
import com.demo.web.io.ByteBufferBackendOutputStream
import com.demo.web.parsers.DefaultParserFactory
import com.demo.web.parsers.ParameterParser
import com.demo.web.parsers.ParserFactory
import com.demo.web.parsers.StringParameterParser
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.reflect.Type
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
    private val supportedInterceptors: List<CoroutineInterceptor>
    private val parserFactory: ParserFactory = DefaultParserFactory


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

    private fun getParameter(exchange: HttpServerExchange): Array<Any?> {
        val parameters = exchange.queryParameters
        return paramList.map { paramData ->
            val list = parameters[paramData.name]
            paramData.parser.parse(list?.firstOrNull() ?: "", paramData.default, paramData.required)
        }.toTypedArray()
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

    private val paramList: List<ParameterData>

    data class ParameterData(
        val name: String,
        val parser: ParameterParser<*>,
        val default: String,
        val required: Boolean
    )

    private fun initParamList(function: KFunction<*>): List<ParameterData> {
        val method = function.javaMethod!!
        val parameters = method.parameters
        val types = method.genericParameterTypes
        val size = if (function.isSuspend) parameters.size - 1 else parameters.size
        val list = ArrayList<ParameterData>(size)
        repeat(size) { index ->
            val parameter = parameters[index]
            val annotation = parameter.getAnnotation(RequestParam::class.java)
            list.add(
                ParameterData(
                    annotation.name,
                    parserFactory.getParser(types[index]),
                    annotation.defaultValue,
                    annotation.required
                )
            )
        }
        return list
    }

    init {
        val status = function.findAnnotation<ResponseStatus>()
        defaultStatus = status?.code?.value() ?: 200
        supportedInterceptors = interceptors.filter { it.isSupported(obj, method) }
        paramList = initParamList(function)
    }

    private class DefaultCoroutineResponse(override val httpStatus: Int, override val body: Any?) : CoroutineResponse
}