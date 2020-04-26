package com.zmeid.urbandictionary.util

import android.content.Context
import com.zmeid.urbandictionary.R
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

class ApiErrorMessageGenerator constructor(private val context: Context) {
    fun generateErrorMessage(exception: Exception): String {
        Timber.e(exception)
        var stringResId = R.string.search_there_is_an_error
        when (exception) {
            is UnknownHostException -> {
                stringResId = R.string.no_internet_error
            }
            is HttpException -> {
                when {
                    exception.code() == 401 -> stringResId = R.string.search_unauthorized_error
                    exception.code() == 404 -> stringResId = R.string.search_http_404_not_found_error
                }
            }
        }
        return String.format(context.getString(stringResId), exception.message)
    }
}