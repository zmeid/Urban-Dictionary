package com.zmeid.urbandictionary.util

import timber.log.Timber
import javax.inject.Inject

class TimberLineNumberDebugTree @Inject constructor() : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "(%s:%s)#%s",
            element.fileName,
            element.lineNumber,
            element.methodName
        )
    }
}