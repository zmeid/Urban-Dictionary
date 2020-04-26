package com.zmeid.urbandictionary.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringTrimDelegate : ReadWriteProperty<Any?, String> {

    private var trimmedValue: String = ""

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        trimmedValue = value.trim()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return trimmedValue
    }
}