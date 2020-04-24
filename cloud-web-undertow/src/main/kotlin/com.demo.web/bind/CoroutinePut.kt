package com.demo.web.bind

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Stefan Liu
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@CoroutineMapping(method = RequestMethod.PUT)
annotation class CoroutinePut(
    @get:AliasFor(annotation = CoroutineMapping::class)
    vararg val value: String = [""],
    @get:AliasFor(annotation = CoroutineMapping::class)
    val method: RequestMethod = RequestMethod.PUT
)
