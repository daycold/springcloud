package com.demo.web

import io.undertow.Undertow
import org.apache.catalina.startup.Tomcat
import org.apache.coyote.UpgradeProtocol
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.xnio.SslClientAuthMode
import java.util.stream.Collectors
import javax.servlet.Servlet

/**
 * @author Stefan Liu
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration::class)
class WebCoroutineAutoConfiguration {
    /**
     * undertow 内部使用 httpHandler 做处理器。starter 将servlet包装成了 httpHandler
     * 当添加了 initialHandlerChainWrapper 后该 wrapper 会对包装的 httpHandler 进行一次包装
     */
    @Bean
    @Primary
    @ConditionalOnClass(Servlet::class, Undertow::class, SslClientAuthMode::class)
    fun undertowServletWebServerFactory(): UndertowServletWebServerFactory {
        return UndertowServletWebServerFactory().apply {
            addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer { deploymentInfo ->
                deploymentInfo.addInitialHandlerChainWrapper { handler ->
                    CoroutinePathHandler(handler)
                }
            })
        }
    }

    @Bean
    @Primary
    @ConditionalOnClass(Servlet::class, Tomcat::class, UpgradeProtocol::class)
    fun tomcatServletWebServerFactory(
        connectorCustomizers: ObjectProvider<TomcatConnectorCustomizer>,
        contextCustomizers: ObjectProvider<TomcatContextCustomizer>,
        protocolHandlerCustomizers: ObjectProvider<TomcatProtocolHandlerCustomizer<*>>
    ): TomcatServletWebServerFactory {
        return TomcatServletWebServerFactory().apply {
            tomcatContextCustomizers.add(TomcatContextCustomizer { context ->
                context.wrapperClass = CoroutineTomcateWrapper::class.java.name
            })
            tomcatConnectorCustomizers.addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()))
            tomcatContextCustomizers.addAll(contextCustomizers.orderedStream().collect(Collectors.toList()))
            tomcatProtocolHandlerCustomizers.addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()))
        }
    }
}