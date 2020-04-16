package com.demo.web.io

import org.xnio.channels.StreamSourceChannel
import java.io.IOException
import java.io.InputStream
import java.lang.Integer.min
import java.nio.ByteBuffer

/**
 * read values from channel to buffer and to byteArray
 * Buffer created by pool, which should free it
 * known the size of space to take values, and could clear after read and reuse it (buffer.get would make a buffer reusable)
 * @author Stefan Liu
 */
class ByteBufferBackendInputStream(
    private val channel: StreamSourceChannel,
    private val buffer: ByteBuffer,
    private var totalCount: Int
) : InputStream() {
    init {
        readBuffer()
    }

    override fun available(): Int {
        return totalCount
    }

    // 将数据写入buffer中
    @Throws(IOException::class)
    override fun read(): Int {
        return if (totalCount > 0) {
            prepareBuffer()
            totalCount = -1
            buffer.get().toInt()
        } else {
            -1
        }
    }

    @Throws
    override fun read(bytes: ByteArray, off: Int, len: Int): Int {
        // 没有数据
        if (totalCount <= 0) {
            return -1
        }

        prepareBuffer()
        val countToRead = min(len, buffer.remaining())
        // buffer 将数据写入 bytes
        buffer.get(bytes, off, countToRead)
        totalCount -= countToRead
        return countToRead
    }

    /**
     * 先清空内存，准备将将io的数据写入该内存
     * 当没有所需要的数据时抛出异常
     * 将buffer的当前位置更改为buffer缓冲区的第一个位置，从该位置开始读取数据
     */
    private fun readBuffer() {
        buffer.clear()
        val count = min(totalCount, buffer.limit())
        do {
            // channel 将数据写入buffer
            if (channel.read(buffer) < 0) {
                throw IllegalArgumentException()
            }
        } while (buffer.position() < count)
        buffer.flip()
    }

    // buffer中存在数据时，不做操作，当buffer中无数据时从channel读数据
    private fun prepareBuffer() {
        if (!buffer.hasRemaining()) {
            readBuffer()
        }
    }
}