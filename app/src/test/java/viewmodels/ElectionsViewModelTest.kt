package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import repository.FakeRepository
import util.getOrAwaitValue

@ExperimentalCoroutinesApi
class ElectionsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun setFilter_Option1_setsTheCorrectFilterOption1() {
        //Given a fresh viewModel
        val electionViewModel = ElectionsViewModel(FakeRepository())
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

    @Test
    fun setFilter_Option2_setsTheCorrectFilterOption2() {
        //GIVEN - a fresh viewmodel
        val electionsViewModel = ElectionsViewModel(FakeRepository())

        //WHEN - correct filter is set = 2
        val savedElectionFilter = 2
        electionsViewModel.selectFilter(savedElectionFilter)

        //THEN - correct filter option is set
        val value = electionsViewModel.filter.getOrAwaitValue()
        assertThat(value, `is`(2))
    }


    @Test
    fun setFilter_Option1_setsTheCorrectListOfElections()  {

        //GIVEN a fresh viewmodel
        val electionsViewModel = ElectionsViewModel(FakeRepository())

        //WHEN - filter option 1 is provided
        val filterOptionOne = 1
        electionsViewModel.selectFilter(filterOptionOne)

        //THEN - electionList is displayed
        val listElectionValue = electionsViewModel.filteredElections.getOrAwaitValue()
        assertThat(listElectionValue.size, `is`(3))
        assertThat(listElectionValue.first().name, `is`("Election 0"))

    }

    @Test
    fun setFilter_Option2_setsTheCorrectListOfSavedElections() = runBlockingTest{

        //GIVEN - a fresh ViewModel
        val fakeRepository = FakeRepository()
        val electionsViewModel = ElectionsViewModel(fakeRepository)

        //and a saved election
        fakeRepository.saveThisElection(SavedElection(1))

        //WHEN - filter option 2 is provided
        val filterOption2 = 2
        electionsViewModel.selectFilter(filterOption2)

        //THEN - electionList displays the saved election
        val listSavedElections = electionsViewModel.filteredElections.getOrAwaitValue()
        assertThat(listSavedElections.size, `is`(1))
        assertThat(listSavedElections.first().name, `is`("Election 1"))
        assertThat(listSavedElections.first().id, `is`(1))
    }

    @Test
    fun setFilter_OptionElse_displaysElectionsList() {
        //GIVEN - a fresh ViewModel
        val electionsViewModel = ElectionsViewModel(FakeRepository())

        //WHEN - filter option other than 1 and 2 is provided
        val filterOption = 3
        electionsViewModel.selectFilter(filterOption)

        //THEN - election List displays the Elections
        val resultElectionList = electionsViewModel.filteredElections.getOrAwaitValue()

        assertThat(resultElectionList.size, `is`(3))
        assertThat(resultElectionList.first().id, `is`(0))
    }
}