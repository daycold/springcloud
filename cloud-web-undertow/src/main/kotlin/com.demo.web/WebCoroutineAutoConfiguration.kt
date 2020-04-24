package com.demo.web

import com.demo.web.handlers.CoroutineRoutingHandler
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.stream.Collectors

/**
 * @author Stefan Liu
 */
@Configuration
@ComponentScan
@ConditionalOnWebApplication
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration::class)
class WebCoroutineAutoConfiguration {
    /**
     * undertow 内部使用 httpHandler 做处理器。starter 将servlet包装成了 httpHandler
     * 当添加了 initialHandlerChainWrapper 后该 wrapper 会对包装的 httpHandler 进行一次包装
     */
    @Bean
    @Primary
    fun undertowServletWebServerFactory(applicationContext: ApplicationContext): UndertowServletWebServerFactory {
        return UndertowServletWebServerFactory().apply {
            addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer { deploymentInfo ->
                deploymentInfo.addInitialHandlerChainWrapper { handler ->
                    CoroutineRoutingHandler(applicationContext, handler)
                }
            })
        }
    }

    private fun tomcatServletWebServerFactory(
        connectorCustomizers: ObjectProvider<TomcatConnectorCustomizer>,
        contextCustomizers: ObjectProvider<TomcatContextCustomizer>,
        protocolHandlerCustomizers: ObjectProvider<TomcatProtocolHandlerCustomizer<*>>
    ): TomcatServletWebServerFactory {
        return TomcatServletWebServerFactory().apply {
            tomcatContextCustomizers.add(TomcatContextCustomizer { context ->
                //                context.wrapperClass = CoroutineTomcatWrapper::class.java.name
            })
            tomcatConnectorCustomizers.addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()))
            tomcatContextCustomizers.addAll(contextCustomizers.orderedStream().collect(Collectors.toList()))
            tomcatProtocolHandlerCustomizers.addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()))
        }
    }
}