package com.example.schedulemaster.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.schedulemaster.R

abstract class SingleFragmentActivity : AppCompatActivity() {

    protected abstract fun createFragment(): Fragment

    private val layoutResId: Int
        @LayoutRes
        get() = R.layout.activity_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        logLifecycleMethod("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycleMethod("onStart")
    }

    // transient state
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        logLifecycleMethod("onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        logLifecycleMethod("onRestart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycleMethod("onResume")
    }

    // transient state
    override fun onSaveInstanceState(outState: Bundle) {
        logLifecycleMethod("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        logLifecycleMethod("onPause")
        super.onPause()
    }

    override fun onStop() {
        logLifecycleMethod("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logLifecycleMethod("onDestroy")
        super.onDestroy()
    }

    private fun logLifecycleMethod(event: String) {
        val activityName = javaClass.simpleName
        Log.i("SingleFragmentActivity Lifecycle Methods", "-----------------------NEW LOG---------------------")
        Log.i("SingleFragmentActivity Lifecycle Methods", "$activityName activity started. LifeCycleMethod: $event")
    }



}