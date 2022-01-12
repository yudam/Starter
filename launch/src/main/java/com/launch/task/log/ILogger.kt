package com.sangame.jjhcps.topof.task.launch.log

/**
 * User: maodayu
 * Date: 2022/1/12 9:46
 */
interface ILogger {

    fun showLogger(isshowLog: Boolean)

    fun info(tag: String, message: String)

    fun error(tag: String, message: String)

    fun warn(tag: String, message: String)
}
