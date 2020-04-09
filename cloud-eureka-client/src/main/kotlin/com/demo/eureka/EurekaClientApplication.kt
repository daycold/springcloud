package com.demo.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

/**
 * @author Stefan Liu
 */
@SpringBootApplication
@EnableEurekaClient
class EurekaClientApplication

fun main(args: Array<String>) {
    SpringApplication.run(EurekaClientApplication::class.java, *args)
}