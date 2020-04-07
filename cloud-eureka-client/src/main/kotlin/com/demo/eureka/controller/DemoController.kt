package com.demo.eureka.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Stefan Liu
 */
@RestController
class DemoController {
    @Value("\${server.port}")
    private var port: Int = 0

    @Value("\${name}")
    private var name: String = ""

    @GetMapping("/hi")
    fun home(): String {
        return "hello world $port $name"
    }
}