package com.launch.task

/**
 * User: maodayu
 * Date: 2022/1/11 11:53
 */
object TaskSort {

    /**
     * 对任务进行拓扑排序
     */
    fun sortTask(originList: MutableList<ILTask>): List<ILTask> {
        val graph = Graph(originList.size)
        originList.forEachIndexed { index, lTask ->
            val dependOn = lTask.dependOn()
            if (!dependOn.isNullOrEmpty()) {
                dependOn.forEach {
                    graph.addEdge(getTaskIndex(originList, it), index)
                }
            }
        }
        val newTaskList = mutableListOf<ILTask>()
        val sortList = graph.sortTask()
        sortList.forEach {
            newTaskList.add(originList[it])
        }
        return newTaskList
    }


    /**
     * 获取任务对应的下标
     */
    private fun getTaskIndex(originList: MutableList<ILTask>, task: Class<out ILTask>): Int {
        var currIndex = -1
        originList.forEachIndexed { index, lTask ->
            if (lTask::class.java == task) {
                currIndex = index
            }
        }
        return currIndex
    }
}