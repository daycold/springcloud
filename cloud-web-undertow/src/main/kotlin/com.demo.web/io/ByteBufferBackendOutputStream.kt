package com.demo.web.io

import io.undertow.connector.ByteBufferPool
import io.undertow.connector.PooledByteBuffer
import java.io.IOException
import java.io.OutputStream
import java.lang.Integer.min
import java.nio.ByteBuffer

/**
 * use bufferPool to allocate space because single buffer may not meet needs, each buffer cannot reuse through clearing
 * @author Stefan Liu
 */
class ByteBufferBackendOutputStream(
    private val bufferPool: ByteBufferPool
) : OutputStream() {
    private val buffers: MutableList<PooledByteBuffer> = mutableListOf()
    private var currentBuffer: ByteBuffer

    init {
        bufferPool.allocate().let {
            buffers.add(it)
            currentBuffer = it.buffer
        }
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        doWrite(b)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        doWrite(b, off, len)
    }

    // only release buffers it created, not shut the pool
    override fun close() {
        buffers.forEach { it.close() }
        buffers.clear()
    }

    val byteBuffers: Array<ByteBuffer>
        get() {
            return buffers.map { it.buffer }.toTypedArray()
        }

    private fun doWrite(b: Int) {
        if (currentBuffer.remaining() == 0) {
            allocateBuffer()
        }
        currentBuffer.put(b.toByte())
    }

    private fun doWrite(bytes: ByteArray, off: Int, len: Int) {
        var offset = off
        var length = len
        while (length > 0) {
            if (currentBuffer.remaining() == 0) {
                allocateBuffer()
            }
            val writeBytes = min(currentBuffer.remaining(), length)
            currentBuffer.put(bytes, offset, writeBytes)
            offset += writeBytes
            length -= writeBytes
        }
    }

    private fun allocateBuffer() {
        val pooledByteBuffer = bufferPool.allocate()
        buffers.add(pooledByteBuffer)
        currentBuffer = pooledByteBuffer.buffer
    }
}