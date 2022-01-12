package com.mdy.starter

import android.util.Log
import com.launch.task.ILTask

/**
 * User: maodayu
 * Date: 2022/1/11 14:27
 */
class Init1Task : ILTask() {

    override fun isMain(): Boolean {
        return false
    }

    override fun execute() {
        Thread.sleep(1000)
        Log.i("MDY", "Init1Task------")
    }
}