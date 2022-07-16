package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.TheRepository
import java.lang.IllegalArgumentException

class VoterInfoViewModelFactory(
    private val repository: TheRepository,
    private val electionId: Int,
    private val division: Division
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(repository, electionId, division) as T
        }

        throw IllegalArgumentException("?Unknown ViewModel")
    }

}