package com.demo.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

/**
 * @author Stefan Liu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
open class EurekaFeignApplication

fun main(args: Array<String>) {
    SpringApplication.run(EurekaFeignApplication::class.java, *args)
}