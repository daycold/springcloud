package com.demo.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

/**
 * @author Stefan Liu
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
open class EurekaZuulApplication {
    @Bean
    @LoadBalanced
    open fun getRestTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
    SpringApplication.run(EurekaZuulApplication::class.java, *args)
}