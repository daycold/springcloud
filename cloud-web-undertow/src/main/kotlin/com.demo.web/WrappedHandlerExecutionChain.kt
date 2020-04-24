package com.demo.web

import org.springframework.web.servlet.HandlerExecutionChain
import org.springframework.web.servlet.ModelAndView
import java.lang.reflect.Method
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Stefan Liu
 */
class WrappedHandlerExecutionChain(private val handlerExecutionChain: HandlerExecutionChain) {
    val handler: Any
    private val applyPreHandleMethod: Method
    private val applyPostHandleMethod: Method
    private val applyAfterConcurrentHandlingStartedMethod: Method

    init {
        val clz = handlerExecutionChain::class.java
        val handlerField = clz.getDeclaredField("handler")
        handlerField.isAccessible = true
        this.handler = handlerField.get(handlerExecutionChain)
        this.applyPreHandleMethod = clz.getDeclaredMethod(
            "applyPreHandle", HttpServletRequest::class.java,
            HttpServletResponse::class.java
        )
        this.applyPreHandleMethod.isAccessible = true
        this.applyPostHandleMethod = clz.getDeclaredMethod(
            "applyPostHandle", HttpServletRequest::class.java,
            HttpServletResponse::class.java, ModelAndView::class.java
        )
        this.applyAfterConcurrentHandlingStartedMethod = clz.getDeclaredMethod(
            "applyAfterConcurrentHandlingStarted",
            HttpServletRequest::class.java, HttpServletResponse::class.java
        )
    }

    @Throws(Exception::class)
    fun applyPreHandle(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        return this.applyPreHandleMethod.invoke(handlerExecutionChain, request, response) as Boolean
    }

    @Throws(Exception::class)
    fun applyPostHandle(request: HttpServletRequest, response: HttpServletResponse, mv: ModelAndView) {
        this.applyPostHandleMethod.invoke(handlerExecutionChain, request, response, mv)
    }

    fun applyAfterConcurrentHandlingStarted(request: HttpServletRequest, response: HttpServletResponse) {
        this.applyAfterConcurrentHandlingStartedMethod.invoke(handlerExecutionChain, request, response)
    }

    @Throws(Exception::class)
    fun triggerAfterCompletion(request: HttpServletRequest, response: HttpServletResponse, ex: Exception) {

    }
}