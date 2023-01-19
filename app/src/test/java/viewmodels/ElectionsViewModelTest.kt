package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import repository.FakeRepository
import util.getOrAwaitValue

/*
* Since we are not using AndroidX test library here
* this means its a win for speed.
* */
@ExperimentalCoroutinesApi
class ElectionsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeRepository : FakeRepository

    //Subject under test
    private lateinit var electionsViewModel:ElectionsViewModel

    @Before
    fun setUp() {

        //GIVEN: a fresh ViewModel
        fakeRepository = FakeRepository()

        electionsViewModel = ElectionsViewModel(fakeRepository)
    }

    @Test
    fun setFilter_Option1_setsTheCorrectFilterOption1() {

        //WHEN: a filter is set to option 1
        val allElectionFilter = 1
        electionsViewModel.selectFilter(allElectionFilter)

        //remember to test live data
        //1. use InstantTaskExecutorRule: runs all architecture related background jobs in the same thread
        //this ensures test runs synchronously, and in repeatable order
        //add this dependency: testImplementation "androidx.arch.core:core-testing:$archTestingVersion"
        //2. Live data is observed (add LiveDataUtil class)

        //THEN - verify that the filter option is set to 1
        val value = electionsViewModel.filter.getOrAwaitValue()
        MatcherAssert.assertThat(value, `is`(1))
    }

    @Test
    fun setFilter_Option2_setsTheCorrectFilterOption2() {

        //WHEN - filter is set to 2
        val savedElectionFilter = 2
        electionsViewModel.selectFilter(savedElectionFilter)

        //THEN - verify filter option is set to 2
        val value = electionsViewModel.filter.getOrAwaitValue()
        MatcherAssert.assertThat(value, `is`(2))
    }


    @Test
    fun setFilter_Option1_setsTheCorrectListOfElections()  {

        //WHEN - filter option 1 is provided
        val filterOptionOne = 1
        electionsViewModel.selectFilter(filterOptionOne)

        //THEN - electionList is displayed
        val listElectionValue = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(listElectionValue.size, `is`(3))
        MatcherAssert.assertThat(listElectionValue.first().name, `is`("Election 0"))

    }

    @Test
    fun setFilter_Option2_setsTheCorrectListOfSavedElections() = runBlockingTest{

        //GIVEN: ...and a saved election
        fakeRepository.saveThisElection(SavedElection(1))

        //WHEN - filter option 2 is provided
        val filterOption2 = 2
        electionsViewModel.selectFilter(filterOption2)

        //THEN - electionList displays the saved election
        val listSavedElections = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(listSavedElections.size, `is`(1))
        MatcherAssert.assertThat(listSavedElections.first().name, `is`("Election 1"))
        MatcherAssert.assertThat(listSavedElections.first().id, `is`(1))
    }

    @Test
    fun setFilter_OptionElse_displaysElectionsList() {

        //WHEN - filter option other than 1 and 2 is provided
        val filterOption = 3
        electionsViewModel.selectFilter(filterOption)

        //THEN - election List displays the Elections
        val resultElectionList = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(resultElectionList.size, `is`(3))
        MatcherAssert.assertThat(resultElectionList.first().id, `is`(0))
    }
}