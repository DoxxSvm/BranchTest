package com.oogwayapps.branchthreads.utils

sealed class ResponseResource<T> {

    class Success<T>(val data: T? = null) : ResponseResource<T>()
    class Loading<T>() :
        ResponseResource<T>()

    class Error<T>(val defaultValue: T? = null, val throwable: Throwable) : ResponseResource<T>()
    class Empty<T>:ResponseResource<T>()
}