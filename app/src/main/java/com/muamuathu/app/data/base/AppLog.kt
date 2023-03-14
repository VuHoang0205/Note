package com.muamuathu.app.data.base

import android.annotation.SuppressLint
import android.util.Log

object AppLog {
    private const val TAG = "the_method"

    fun d(msg: String) {
        Log.d(TAG, codeLocation + msg)
    }

    fun i(msg: String) {
        Log.i(TAG, codeLocation + msg)
    }

    fun w(msg: String) {
        Log.w(TAG, codeLocation + msg)
    }

    fun e(msg: String) {
        Log.e(TAG, codeLocation + msg)
    }

    fun v(msg: String) {
        Log.v(TAG, codeLocation + msg)
    }

    @SuppressLint("LogNotTimber")
    fun printStackTrace(throwable: Throwable) {
        Log.e(
            TAG,
            "**************************************************************************************************************************************************************************************"
        )
        for (ste in throwable.stackTrace) {
            Log.d(TAG, ste.toString())
        }
        Log.e(
            TAG,
            "**************************************************************************************************************************************************************************************"
        )
    }

    private val codeLocation: String
        get() {
            val thread = Thread.currentThread()
            val fullClassName = thread.stackTrace[4].className
            val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
            val methodName = thread.stackTrace[4].methodName
            val lineNumber = thread.stackTrace[4].lineNumber
            return "$className.$methodName():$lineNumber: "
        }
}

