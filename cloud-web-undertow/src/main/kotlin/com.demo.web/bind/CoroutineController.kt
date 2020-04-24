package com.demo.web.bind

import org.springframework.web.bind.annotation.RestController

/**
 * @author Stefan Liu
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RestController
annotation class CoroutineController
