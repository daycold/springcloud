package com.demo.eureka.controller

import com.demo.eureka.service.DemoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Stefan Liu
 */
@RestController
class DemoController(
    private val demoService: DemoService
) {
    @GetMapping("hi")
    fun hi(): String = demoService.sayHi()
}