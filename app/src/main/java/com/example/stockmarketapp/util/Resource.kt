package com.example.stockmarketapp.util


sealed class Resource <T>(val data: T?, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data,null)
    class Error<T>(message: String) : Resource<T>(null, message)
    class Loading<T>(val isLoading: Boolean = true): Resource<T>(null)
}
