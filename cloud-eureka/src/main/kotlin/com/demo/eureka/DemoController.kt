package com.demo.eureka

import com.demo.web.CoroutineController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author Stefan Liu
 */
//@RestController
@CoroutineController
@RequestMapping
class DemoController {
    @GetMapping("hi")
    suspend fun sayHi() = "name"
}