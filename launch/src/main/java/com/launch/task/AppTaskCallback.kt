package com.launch.task

/**
 * User: maodayu
 * Date: 2022/1/12 11:45
 * SDK执行回调
 */
interface AppTaskCallback {

    /**
     * @param access true表示可以跳转主界面
     */
    fun runToMain(task: ILTask, access: Boolean)
}