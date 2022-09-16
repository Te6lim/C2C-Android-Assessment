package com.te6lim.c2candroidassessment.screens

import androidx.lifecycle.*
import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.repository.ExhibitRepository

class ExhibitListViewModel(val exhibitRepository: ExhibitRepository) : ViewModel() {

    val exhibitList = Transformations.map(exhibitRepository.exhibitList) {
        it.map { exhibit ->
            Exhibit(title = exhibit.title!!, images = exhibit.images!!)
        }
    }

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