package com.demo.web.handlers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KFunction

/**
 * @author Stefan Liu
 */
class RequestMappingData(val path: Array<String>, val method: RequestMethod) {
    companion object {
        @JvmStatic
        fun getRequestMappingAnnotation(function: KFunction<*>): RequestMappingData? {
            for (annotation in function.annotations) {
                if (annotation is RequestMapping) {
                    return RequestMappingData(annotation.value, annotation.method[0])
                }
                for (anno in annotation.annotationClass.annotations) {
                    if (anno is RequestMapping) {
                        return RequestMappingData(
                            annotation.javaClass.getDeclaredMethod("value").invoke(annotation) as Array<String>,
                            anno.method[0]
                        )
                    }
                }
            }
            return null
        }
    }
}