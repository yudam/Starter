package com.launch.task

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService

/**
 * User: maodayu
 * Date: 2022/1/10 17:22
 */
abstract class ILTask : LTask {

    /**
     * 用户同步监听依赖任务是否完成
     */
    private val dependLatch: CountDownLatch =
        CountDownLatch(if (dependOn().isNullOrEmpty()) 0 else dependOn().size)

    /**
     * 当前任务等待依赖任务完成，若不存在依赖任务，会直接继续执行
     */
    fun await() {
        dependLatch.await()
    }

    /**
     * 依赖任务完成，锁状态-1，判断是否执行当前任务
     */
    fun countdown() {
        dependLatch.countDown()
    }

    /**
     * out 协变 指定上界通配符
     * in  逆变 指定下界通配符
     */
    override fun dependOn(): List<Class<out ILTask>> {
        return emptyList()
    }

    override fun runExecutors(): ExecutorService? {
        return null
    }

    override fun immediately(): Boolean {
        return false
    }

    override fun waitForApplication(): Boolean {
        return true
    }

    override fun isMain(): Boolean {
        return false
    }

    override fun isFinish(): Boolean {
        return false
    }

    override fun isStart(): Boolean {
        return false
    }

}