package com.sangame.jjhcps.topof.task.launch.pools

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * User: maodayu
 * Date: 2022/1/11 15:23
 */
object StarterExecutor {

    private val threadNumber = AtomicInteger(1)

    private val handler = RejectedExecutionHandler { r, executor ->
        executor.shutdown()
        executor.shutdownNow()
        if (!executor.isShutdown) {
            executor.queue.poll()
            executor.execute(r)
        }
    }

    /**
     * 启动器线程池
     */
    fun createDefaultPools(): ExecutorService {
        val factory =
            ThreadFactory { r ->
                Thread(r,
                    "starter-" + threadNumber.getAndIncrement() + "-thread")
            }

        return ThreadPoolExecutor(
            0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue(), factory)
    }

}