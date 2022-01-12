package com.sangame.jjhcps.topof.task.launch.log

import android.text.TextUtils
import android.util.Log

/**
 * User: maodayu
 * Date: 2022/1/12 9:42
 */
class DispatcherLog : ILogger {

    private val defaultTag: String = "Launch"
    private var isShowLog: Boolean = false

    override fun showLogger(isshowLog: Boolean) {
        isShowLog = isshowLog
    }

    override fun info(tag: String, message: String) {
        if (isShowLog) {
            Log.i(if (TextUtils.isEmpty(tag)) getDefaultTag() else tag, message)
        }
    }

    override fun error(tag: String, message: String) {
        if (isShowLog) {
            Log.e(if (TextUtils.isEmpty(tag)) getDefaultTag() else tag, message)
        }
    }

    override fun warn(tag: String, message: String) {
        if (isShowLog) {
            Log.w(if (TextUtils.isEmpty(tag)) getDefaultTag() else tag, message)
        }
    }

    private fun getDefaultTag(): String {
        return defaultTag
    }
}