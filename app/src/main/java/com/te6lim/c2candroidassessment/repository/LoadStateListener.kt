package com.te6lim.c2candroidassessment.repository

interface LoadStateListener {

    fun onStateResolved(state: LoadState, source: LoadSource)

    fun onRefresh(isSuccess: Boolean)
}