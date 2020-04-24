package com.demo.web.bind

import org.springframework.web.bind.annotation.RequestMethod
import java.lang.annotation.Inherited

/**
 * @author Stefan Liu
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class CoroutineMapping(
    vararg val value: String = [""],
    val method: RequestMethod
)
