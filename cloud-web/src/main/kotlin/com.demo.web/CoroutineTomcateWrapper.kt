package com.demo.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.catalina.core.StandardWrapper
import javax.servlet.Servlet
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * @author Stefan Liu
 */
class CoroutineTomcateWrapper : StandardWrapper() {
    override fun setServlet(servlet: Servlet) {
        super.setServlet(CoroutineServlet(servlet))
    }

    private class CoroutineServlet(private val servlet: Servlet) : Servlet by servlet {
        override fun service(req: ServletRequest, res: ServletResponse) {
            val ctx = req.startAsync()
            CoroutineScope(CoroutineUtils.COROUTINE_CONTEXT).launch {
                servlet.service(req, res)
                ctx.complete()
            }
        }
    }
}

