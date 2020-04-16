package com.demo.web.handlers

import com.demo.web.CoroutineController
import io.undertow.server.HttpHandler
import io.undertow.server.RoutingHandler
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.RequestMapping
import kotlin.reflect.full.declaredFunctions

/**
 * @author Stefan Liu
 */
class CoroutineRoutingHandler(context: ApplicationContext, defaultHandler: HttpHandler) : RoutingHandler(false) {
    init {
        fallbackHandler = defaultHandler
        invalidMethodHandler = null
        val beans = context.getBeansWithAnnotation(CoroutineController::class.java).values
        beans.forEach { controller ->
            val clazzKt = controller::class
            val clazz = clazzKt.java
            val requestMapping = clazz.getDeclaredAnnotationsByType(RequestMapping::class.java)
                ?.firstOrNull() ?: return@forEach
            val prefixValues = requestMapping.value
            val prefix = if (prefixValues.isEmpty()) arrayOf("") else prefixValues
            clazzKt.declaredFunctions.forEach eachFunction@{ function ->
                val methodMapping = RequestMappingData.getRequestMappingAnnotation(function)
                    ?: return@eachFunction
                val suffix = methodMapping.path
                val handler = CoroutineFunctionHandler(function, controller)
                val httpMethod = methodMapping.method.name
                prefix.forEach { pre ->
                    suffix.forEach { suf ->
                        add(httpMethod, pre + suf, handler)
                    }
                }
            }
        }
    }
}
