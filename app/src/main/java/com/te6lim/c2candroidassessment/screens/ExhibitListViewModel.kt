package com.te6lim.c2candroidassessment.screens

import androidx.lifecycle.*
import com.te6lim.c2candroidassessment.model.Exhibit
import com.te6lim.c2candroidassessment.model.RestExhibitLoader
import kotlinx.coroutines.launch

class ExhibitListViewModel(private val exhibitLoader: RestExhibitLoader) : ViewModel() {

    private val _exhibitList = MutableLiveData<List<Exhibit>>()
    val exhibitList: LiveData<List<Exhibit>>
        get() = _exhibitList

    val errorOccurred = Transformations.map(_exhibitList) {
        it.isEmpty()
    }

    init {
        refreshList()
    }

    fun refreshList() {
        viewModelScope.launch {
            _exhibitList.postValue(exhibitLoader.getExhibitList())
        }
    }

    class Factory(private val exhibitLoader: RestExhibitLoader) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExhibitListViewModel::class.java))
                return ExhibitListViewModel(exhibitLoader) as T
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}