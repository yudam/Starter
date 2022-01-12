package com.launch.task

import android.os.Build
import android.os.Handler
import android.os.Looper
import com.sangame.jjhcps.topof.task.launch.log.DispatcherLog
import com.sangame.jjhcps.topof.task.launch.log.ILogger
import com.sangame.jjhcps.topof.task.launch.pools.StarterExecutor
import com.sangame.jjhcps.topof.task.launch.stat.StatNotepad
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicInteger

/**
 * User: maodayu
 * Date: 2022/1/11 13:53
 */
object TaskDispatchers {

    //Log Tag
    private var TAG = "TaskDispatchers"
    private var logger: ILogger = DispatcherLog()

    //记录初始化的SDK数量
    private val appTaskCtl = AtomicInteger(0)

    //Application中具体需要执行的任务数量
    private var appCount: Int = 0

    //是否初始化
    private var isInit: Boolean = false

    //保存所有的任务
    private var mAllTaskList = mutableListOf<ILTask>()

    /**
     * @param key  当前任务类对象
     * @param value 依赖当前任务的 任务类对象集合
     */
    private var mPreTaskMap = hashMapOf<Class<out ILTask>, MutableList<Class<out ILTask>>>()
    private var executor: ExecutorService? = null
    private var mainHandler: Handler? = null
    private var sdkCallback: AppTaskCallback? = null

    /**
     * 初始化
     */
    fun init(logTag: String = TAG,
             ect: ExecutorService = createIoExecuter(),
             callback: AppTaskCallback? = null): TaskDispatchers {
        isInit = true
        TAG = logTag
        executor = ect
        mainHandler = Handler(Looper.getMainLooper())
        if (callback != null) {
            sdkCallback = callback
        }
        return this
    }

    /**
     * 是否打印Log
     */
    fun debuggable(showLog: Boolean): TaskDispatchers {
        logger.showLogger(showLog)
        return this
    }

    /**
     * 添加任务
     */
    fun addTask(task: ILTask): TaskDispatchers {
        mAllTaskList.add(task)
        val dependTaskList = task.dependOn()
        dependTaskList.forEach {
            if (mPreTaskMap[it] == null) {
                mPreTaskMap[it] = mutableListOf()
            }
            mPreTaskMap[it]?.add(task::class.java)
        }
        return this
    }

    /**
     * 启动任务链路
     */
    fun start() {
        check(isInit) { "Please initialize first" }
        appCount = mAllTaskList.filter { it.waitForApplication() }.size
        val newTaskList = TaskSort.sortTask(mAllTaskList)
        newTaskList.forEach {
            if (it.isMain()) {
                if (it.immediately()) {
                    mainHandler?.post {
                        startTask(it)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mainHandler?.looper?.queue?.addIdleHandler {
                            startTask(it)
                            false
                        }
                    } else {
                        mainHandler?.post {
                            startTask(it)
                        }
                    }
                }
            } else {
                it.runExecutors()?.execute {
                    startTask(it)
                } ?: executor?.execute {
                    startTask(it)
                }
            }
        }
    }

    /**
     * 执行SDK初始化
     */
    private fun startTask(task: ILTask) {
        try {
            var thrown: Throwable? = null
            val preTime = System.currentTimeMillis()
            task.await()
            beforeExecute(task)
            val startTime = System.currentTimeMillis()
            try {
                task.execute()
            } catch (e: Exception) {
                thrown = e
            } finally {
                afterExecute(task, thrown)
            }
            clipLTask(task)
            //记录任务的执行和等待时间
            StatNotepad.recordSdkTime(task,
                startTime - preTime,
                System.currentTimeMillis() - startTime)
        } finally {
            if (task.waitForApplication()) {
                appTaskCtl.getAndIncrement()
            }
            sdkCallback?.runToMain(task, appTaskCtl.get() == appCount)
        }
    }

    private fun beforeExecute(task: ILTask) {
        logger.info(TAG, "${task::class.java.canonicalName} is start run")
    }

    private fun afterExecute(task: ILTask, thrown: Throwable?) {
        logger.info(TAG, "${task::class.java.canonicalName} is end")
        thrown?.let {
            logger.error(TAG, it.message ?: "")
        }
    }


    /**
     * 通知入度任务，依赖任务执行完毕
     */
    private fun clipLTask(task: ILTask) {
        mPreTaskMap[task::class.java]?.forEach {
            val lastTask = mAllTaskList.first { item ->
                item::class.java == it
            }
            lastTask.countdown()
        }
    }

    /**
     * 创建IO线程池
     */
    private fun createIoExecuter(): ExecutorService {
        return StarterExecutor.createDefaultPools()
    }
}