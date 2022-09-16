package com.te6lim.c2candroidassessment.repository

interface LoadStateListener {

    fun onStateResolved(state: ExhibitRepository.NetworkState)
}