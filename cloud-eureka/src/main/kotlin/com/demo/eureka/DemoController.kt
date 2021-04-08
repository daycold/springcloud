package com.demo.eureka

//import com.demo.web.bind.CoroutineController
//import com.demo.web.bind.CoroutineGet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @author Stefan Liu
 */
//@CoroutineController
@RequestMapping
@RestController
class DemoController {
//    @CoroutineGet("hi")
    @GetMapping("hi")
    suspend fun sayHi(@RequestParam(name = "name") name: String) = "hi $name"

    @GetMapping("sayHi")
    fun hi() = "hi"
}