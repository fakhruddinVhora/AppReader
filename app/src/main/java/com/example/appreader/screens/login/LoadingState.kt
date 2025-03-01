package com.example.appreader.screens.login

data class LoadingState(val status: Status, val message: String? = null) {
    enum class Status {
        IDLE,
        LOADING,
        SUCCESS,
        FAILED
    }

    companion object {
        val IDLE = LoadingState(Status.IDLE)
        val LOADING = LoadingState(Status.LOADING)
        val SUCCESS = LoadingState(Status.SUCCESS)
        fun failed(message: String) = LoadingState(Status.FAILED, message)
    }
}
