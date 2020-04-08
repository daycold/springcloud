package com.demo.web

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * @author Stefan Liu
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration::class)
open class UndertowAutoConfiguration {
    @Bean
    @Primary
    open fun undertowServletWebServerFactory(): UndertowServletWebServerFactory {
        return UndertowServletWebServerFactory().apply {
            addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer { deploymentInfo ->
                deploymentInfo.addInitialHandlerChainWrapper { handler ->
                    CoroutinePathHandler(handler)
                }
            })
        }
    }
}