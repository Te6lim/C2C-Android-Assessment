package com.te6lim.c2candroidassessment.screens.exhibitList

import androidx.lifecycle.*
import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.ExhibitRepository

class ExhibitListViewModel(private val exhibitRepository: ExhibitRepository) : ViewModel() {

    val exhibitList = exhibitRepository.exhibitList

    fun refreshList() {
        exhibitRepository.refreshList()
    }

    class Factory(private val exhibitRepository: ExhibitRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExhibitListViewModel::class.java))
                return ExhibitListViewModel(exhibitRepository) as T
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}