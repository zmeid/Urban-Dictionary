package com.zmeid.urbandictionary.util

// TODO KotlinDoc
data class ApiResponseWrapper<out T>(val status: Status, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

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