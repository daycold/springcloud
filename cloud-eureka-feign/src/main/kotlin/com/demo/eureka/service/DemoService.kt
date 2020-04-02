package com.demo.eureka.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author Stefan Liu
 */
@FeignClient(value = "cloud-eureka-client", fallback = DemoFallbackService::class)
interface DemoService {
    @GetMapping("/hi")
    fun sayHiFromClient(): String
}