package com.demo.web

import com.demo.web.bind.CoroutineController
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 * @author Stefan Liu
 */
//@Component
class CoroutineControllerPostProcessor : BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val beanName = EnableWebMvcConfiguration::class.java.name
        beanFactory.getBeanDefinition(beanName).apply {
            beanClassName = EnableCoroutineWebConfiguration::class.java.name
        }
    }

    class EnableCoroutineWebConfiguration(
        resourceProperties: ResourceProperties?,
        mvcPropertiesProvider: ObjectProvider<WebMvcProperties>?,
        mvcRegistrationsProvider: ObjectProvider<WebMvcRegistrations>?,
        beanFactory: ListableBeanFactory?
    ) : EnableWebMvcConfiguration(resourceProperties, mvcPropertiesProvider, mvcRegistrationsProvider, beanFactory) {
        override fun createRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
            return CoroutineMappingHandlerMapping()
        }
    }

    class CoroutineMappingHandlerMapping : RequestMappingHandlerMapping() {
        override fun isHandler(beanType: Class<*>): Boolean {
            return super.isHandler(beanType)
                    && !AnnotatedElementUtils.hasAnnotation(beanType, CoroutineController::class.java)
        }
    }
}