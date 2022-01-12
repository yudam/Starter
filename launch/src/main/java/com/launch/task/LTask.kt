package com.launch.task

import java.util.concurrent.ExecutorService

/**
 * User: maodayu
 * Date: 2022/1/10 17:20
 */
interface LTask {

    /**
     * 当前任务依赖的前置任务
     */
    fun dependOn(): List<Class<out LTask>>

    /**
     * 指定任务运行线程池
     */
    fun runExecutors(): ExecutorService?

    /**
     * 是否立刻执行，针对主线程任务
     */
    fun immediately(): Boolean

    /**
     * 是否需要在Application执行
     */
    fun waitForApplication(): Boolean

    /**
     * 运行线程
     */
    fun isMain(): Boolean

    /**
     * 是否结束
     */
    fun isFinish(): Boolean

    /**
     * 是否开始
     */
    fun isStart(): Boolean

    /**
     * 执行内容
     */
    fun execute()
}