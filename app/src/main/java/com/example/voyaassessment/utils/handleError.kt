package com.example.voyaassessment.utils

import retrofit2.Response

fun <T : Any?> handleError(response: Response<T>): String? {
        val errorBody = response.errorBody()?.string()
        val errorMessage = if (!errorBody.isNullOrEmpty()) {
            try {
                val errorResponse = Tools.handleErrorResponse(errorBody)
                errorResponse?.message
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        return errorMessage
    }