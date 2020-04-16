package com.demo.web

/**
 * @author Stefan Liu
 */
interface CoroutineResponse {
    val httpStatus: Int
    val body: Any?
}