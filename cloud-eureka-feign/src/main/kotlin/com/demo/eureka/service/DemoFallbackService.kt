package com.demo.eureka.service

import org.springframework.stereotype.Component

/**
 * @author Stefan Liu
 */
@Component
class DemoFallbackService : DemoService {
    override fun sayHiFromClient(): String = "error"
}