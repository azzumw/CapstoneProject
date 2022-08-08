package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.repository.TheRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue

class ElectionsViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setfilter_setsTheCorrectFilter(){
        //Given a fresh viewModel
        //TODO: create a fake repository
        val electionViewModel = ElectionsViewModel()
        //WHEN: a filter is set
        val allElectionFilter = 1
        electionViewModel.selectFilter(allElectionFilter)

        //THEN: correct list livedata is shown/triggered
        //remember to test live data
        //1. use InstantTaskExecutorRule: runs all architecture related background jobs in the same thread
        //this ensures test runs synchronously, and in repeatable order
        //add this dependency: testImplementation "androidx.arch.core:core-testing:$archTestingVersion"
        //2. Live data is observed (add LiveDataUtil class)
        val value = electionViewModel.filter.getOrAwaitValue()
        assertThat(1, `is`(value))
    }
}