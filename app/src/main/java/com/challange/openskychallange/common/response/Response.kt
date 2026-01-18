package com.challange.openskychallange.common.response

sealed interface Response<out T>{
    data object Loading : Response<Nothing>
    data class Success<T>(val data : T) : Response<T>
    data class Failure<T>(val exception: Exception ?= null,val errorMessage : String? = null) : Response<T>
}