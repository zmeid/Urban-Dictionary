package com.zmeid.urbandictionary.util

import timber.log.Timber
import javax.inject.Inject

/**
 * Provides clickable log tree to navigate developer to the point where log was generated. Also provides fileName, lineNumber, methodName.
 */
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