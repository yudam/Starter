package com.mdy.starter

import java.util.*

/**
 * User: maodayu
 * Date: 2022/1/11
 * Time: 21:04
 * 拓扑排序 G(V,E) V表示任务集合，E表示边集合
 */
class DAG_Graph(private val taskCount: Int) {

    /**
     * 数组下标表示具体的任务，集合表示依赖的任务
     */

    private val topologyArr = arrayOfNulls<MutableList<Int>>(taskCount)


    init {
        for (index in 0 until taskCount) {
            topologyArr[index] = mutableListOf()
        }
    }

    /**
     * 添加边
     * @param from 表示来源任务
     * @param to 表示入度任务
     */
    fun addEdge(from: Int, to: Int) {
        topologyArr[from]?.add(to)
    }

    fun sortGraph(): MutableList<Int> {

        val indegrees = IntArray(taskCount)

        for (index in 0 until taskCount) {
            topologyArr[index]?.forEach {
                indegrees[it]++
            }
        }

        //添加入度为0的节点
        val queue = ArrayDeque<Int>()
        indegrees.forEach {
            if (it == 0) {
                queue.add(it)
            }
        }
        var modCount = 0
        val newTaskList = mutableListOf<Int>()
        while (!queue.isEmpty()) {
            modCount++
            val currTask = queue.poll()
            newTaskList.add(currTask)
            //依赖当前任务的入度减1，入度为0的任务添加到队列中
            topologyArr[currTask]?.forEach {
                if (--indegrees[it] == 0) {
                    queue.add(it)
                }
            }
            check(modCount <= taskCount) {
                "任务存在环"
            }
        }

        //modCount表示任务的数量，当小于taskCount表示有任务没添加
        check(modCount == taskCount) {
            "任务无法全部执行"
        }

        return newTaskList
    }
}