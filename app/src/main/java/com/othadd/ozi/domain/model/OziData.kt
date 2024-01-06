package com.othadd.ozi.domain.model

sealed class OziData<T>(val data: T? = null) {
    class Error<T> : OziData<T>()
    class Fetching<T> : OziData<T>()
    class Available<T>( data: T ): OziData<T>(data)
}