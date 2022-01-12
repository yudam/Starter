package com.launch.task

import java.util.*

/**
 * User: maodayu
 * Date: 2022/1/10 18:50
 * G(V,E) V表示顶点集合，E表示边集合
 */
class Graph(private val mITaskCount: Int) {

    /**
     * 坐标表示任务，集合表示以当前任务为前置条件的任务集合
     */
    private var preTask = arrayOfNulls<MutableList<Int>?>(mITaskCount)

    init {
        for (index in 0 until mITaskCount) {
            preTask[index] = mutableListOf()
        }
    }

    /**
     * 添加任务
     * @param from 前置任务
     * @param to 后置任务
     */
    fun addEdge(from: Int, to: Int) {
        preTask[from]?.add(to)
    }

    /**
     * 返回排序集合
     */
    fun sortTask(): List<Int> {
        val indegrees = IntArray(mITaskCount)
        //计算每个任务的入度
        for (index in 0 until mITaskCount) {
            preTask[index]?.forEach {
                indegrees[it]++
            }
        }
        val queue: Queue<Int> = ArrayDeque()
        //添加入度为0的任务到队列中
        for (index in 0 until mITaskCount) {
            if (indegrees[index] == 0) {
                queue.add(index)
            }
        }

        var midCount = 0
        val result = mutableListOf<Int>()
        while (!queue.isEmpty()) {
            val currTask = queue.poll()
            midCount++
            //添加到列表中
            result.add(currTask)
            //当前任务对应的边集合的任务入度全部减1
            preTask[currTask]?.forEach {
                if (--indegrees[it] == 0) {
                    queue.add(it)
                }
            }
            if (midCount > mITaskCount) break
        }
        //任务存在闭环，直接抛出异常
        check(midCount == mITaskCount) {
            "Missions have closed loops"
        }
        return result
    }

}