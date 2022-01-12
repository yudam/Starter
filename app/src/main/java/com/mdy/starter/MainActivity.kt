package com.mdy.starter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.launch.task.TaskDispatchers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun send(view: View) {
        setRunTaskDependence2()
    }

    private fun setRunTaskDependence2() {
        TaskDispatchers.init()
            .debuggable(true)
            .addTask(Init1Task())
            .addTask(Init2Task())
            .addTask(Init3Task())
            .addTask(Init4Task())
            .addTask(Init5Task())
            .start()
    }
}