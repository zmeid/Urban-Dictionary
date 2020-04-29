package com.zmeid.urbandictionary.util

/**
 * [ApiResponseWrapper] is used to wrap all API responses. It has enum class [Status] which tells us the current status of the API call. When the call is made and waiting for response, the [Status] is [Status.LOADING], when response is success [Status.SUCCESS] and if api call fails it is [Status.ERROR].
 *
 */
data class ApiResponseWrapper<out T>(val status: Status, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    /**
     * Holds static functions to set [ApiResponseWrapper]'s state, data and exception variables.
     */
    companion object {
        fun <T> success(data: T, exception: Exception? = null): ApiResponseWrapper<T> {
            return ApiResponseWrapper(Status.SUCCESS, data, exception)
        }

        fun <T> error(data: T? = null, exception: Exception): ApiResponseWrapper<T> {
            return ApiResponseWrapper(Status.ERROR, data, exception)
        }

        fun <T> loading(data: T? = null, exception: Exception? = null): ApiResponseWrapper<T> {
            return ApiResponseWrapper(Status.LOADING, data, exception)
        }
    }
}