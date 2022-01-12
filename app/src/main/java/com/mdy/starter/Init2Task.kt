package com.mdy.starter

import android.util.Log
import com.launch.task.ILTask

/**
 * User: maodayu
 * Date: 2022/1/11 14:28
 */
class Init2Task : ILTask() {

    override fun dependOn(): List<Class<out ILTask>> {
        return listOf(Init1Task::class.java)
    }

    override fun execute() {
        Thread.sleep(2000)
        Log.i("MDY", "Init2Task------")
    }
}