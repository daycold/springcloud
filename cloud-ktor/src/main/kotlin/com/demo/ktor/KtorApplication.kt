package com.demo.ktor

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

/**
 * @author Stefan Liu
 */
fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        routing {
            get("/") {
                call.respondText("hello, world!", ContentType.Text.Html)
            }

            get("/{name}") {
                val name = context.parameters["name"]
                call.respondText("hello, $name", ContentType.Text.Html)
            }
        }
    }.start()
}