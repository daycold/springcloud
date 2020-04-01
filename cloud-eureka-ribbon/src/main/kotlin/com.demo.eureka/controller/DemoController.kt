package com.demo.eureka.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

/**
 * @author Stefan Liu
 */
@RestController
class DemoController(
    private val restTemplate: RestTemplate
) {
    @GetMapping("hi")
    fun hi(): String =
        restTemplate.getForObject("http://CLOUD-EUREKA-CLIENT/hi", String::class.java)
            ?: "failed"
}