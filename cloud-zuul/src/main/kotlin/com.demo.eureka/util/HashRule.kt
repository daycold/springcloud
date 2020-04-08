package com.demo.eureka.util

import com.netflix.client.config.IClientConfig
import com.netflix.loadbalancer.AbstractLoadBalancerRule
import com.netflix.loadbalancer.Server
import com.netflix.zuul.context.RequestContext

/**
 * @author Stefan Liu
 */
class HashRule : AbstractLoadBalancerRule() {
    override fun initWithNiwsConfig(clientConfig: IClientConfig) {
    }

    override fun choose(key: Any): Server {
        val services = loadBalancer.reachableServers
        val context = RequestContext.getCurrentContext()
        return services[0]
    }
}