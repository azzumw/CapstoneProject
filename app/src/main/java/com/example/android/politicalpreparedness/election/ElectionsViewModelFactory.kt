package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import com.example.android.politicalpreparedness.repository.TheRepository
import java.lang.IllegalArgumentException

class ElectionsViewModelFactory(private val repository: RepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ElectionsViewModel::class.java)){
            return ElectionsViewModel(repository) as T
        }
    throw IllegalArgumentException("Unknown ViewModel")
    }

}