package com.demo.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import zipkin2.server.internal.EnableZipkinServer

/**
 * @author Stefan Liu
 */
@SpringBootApplication
@EnableZipkinServer
open class ZipkinApplication

fun main(args: Array<String>) {
    SpringApplication.run(ZipkinApplication::class.java, *args)
}