package com.demo.web

import org.springframework.stereotype.Component

/**
 * @author Stefan Liu
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class CoroutineController
