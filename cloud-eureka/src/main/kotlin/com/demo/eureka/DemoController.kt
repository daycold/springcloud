package com.demo.eureka

import com.demo.web.bind.CoroutineController
import com.demo.web.bind.CoroutineGet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Stefan Liu
 */
@CoroutineController
@RequestMapping
class DemoController {
    @CoroutineGet("hi")
    suspend fun sayHi() = "name"

    @GetMapping("sayHi")
    fun hi() = "hi"
}