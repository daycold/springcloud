package com.demo.admin

import de.codecentric.boot.admin.server.config.EnableAdminServer
import io.undertow.server.handlers.PathHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author Stefan Liu
 */
@SpringBootApplication
@EnableAdminServer
class AdminApplication

fun main(args: Array<String>) {
    SpringApplication.run(AdminApplication::class.java, *args)
}