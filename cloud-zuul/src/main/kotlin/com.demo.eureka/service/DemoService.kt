package com.demo.eureka.service

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author Stefan Liu
 */
@Service
open class DemoService (private val restTemplate: RestTemplate) {
    @HystrixCommand(fallbackMethod = "hiError")
    open fun sayHi() =
        restTemplate.getForObject("http://CLOUD-EUREKA-CLIENT/hi", String::class.java)
            ?: "failed"

    fun hiError() = "error"
}