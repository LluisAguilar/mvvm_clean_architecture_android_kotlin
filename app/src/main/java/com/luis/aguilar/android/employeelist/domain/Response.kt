package com.luis.aguilar.android.employeelist.domain

sealed class Response<out T> {

    data class Success<out T>(val data: T) : Response<T>() {
        override fun succeeded(): Boolean {
            return true
        }
    }

    data class Error(val exception: Throwable) : Response<Nothing>()

    object NotInitialized : Response<Nothing>()

    object Loading : Response<Nothing>()

    open fun succeeded(): Boolean {
        return false
    }

}

