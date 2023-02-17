package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import repository.FakeRepository
import util.getOrAwaitValue
import java.util.*

/*
* Since we are not using AndroidX test library here
* this means its a win for speed.
* */



@ExperimentalCoroutinesApi
class ElectionsViewModelTest {

    //remember to test live data
    //1. use InstantTaskExecutorRule: runs all architecture related background jobs in the same thread
    //this ensures test runs synchronously, and in repeatable order
    //add this dependency: testImplementation "androidx.arch.core:core-testing:$archTestingVersion"
    //2. Live data is observed (add LiveDataUtil class)
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeRepository: FakeRepository

    //Subject under test
    private lateinit var electionsViewModel: ElectionsViewModel

    @Before
    fun setUp() {

        //creating dummy elections lists
        val electionsList = createSomeElections()

        //setting up the fake repository
        fakeRepository = FakeRepository(electionsList)

        electionsViewModel = ElectionsViewModel(fakeRepository)
    }


    @Test
    fun selectFilter_when_input_is_all_elections_saves_the_given_filter() {

        //WHEN: a filter is set to option 1
        electionsViewModel.selectFilter(ALL_ELECTIONS)

        //THEN - verify that the filter option is set to 1
        val value = electionsViewModel.filter.getOrAwaitValue()
        MatcherAssert.assertThat(value, `is`(1))
    }

    @Test
    fun selectFilter_when_input_is_saved_elections_saves_the_given_filter() {

        //WHEN - filter is set to 2
        electionsViewModel.selectFilter(SAVED_ELECTIONS)

        //THEN - verify filter option is set to 2
        val value = electionsViewModel.filter.getOrAwaitValue()
        MatcherAssert.assertThat(value, `is`(2))
    }


    @Test
    fun selectFilter_filter_with_option_one_displays_all_elections_list() {

        //WHEN - filter option 1 is provided
        electionsViewModel.selectFilter(ALL_ELECTIONS)

        //THEN - electionList is displayed
        val listElectionValue = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(listElectionValue.size, `is`(3))
        MatcherAssert.assertThat(listElectionValue.first().name, `is`("Election 0"))

    }

    @Test
    fun selectFilter_filter_with_option_two_displays_the_given_saved_election() = runTest {

        //GIVEN: a saved election
        fakeRepository.saveThisElection(SavedElection(2))

        //WHEN - filter option 2 is provided
        electionsViewModel.selectFilter(SAVED_ELECTIONS)

        //THEN - electionList displays the saved election
        val listSavedElections = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(listSavedElections.size, `is`(1))
        MatcherAssert.assertThat(listSavedElections.first().name, `is`("Election 2"))
        MatcherAssert.assertThat(listSavedElections.first().name, not("Election 1"))
        MatcherAssert.assertThat(listSavedElections.first().id, `is`(2))
    }

    @Test
    fun selectFilter_option_neither_one_or_two_displays_the_all_elections_list() {

        //WHEN - filter option other than 1 and 2 is provided
        val filterOption = 3
        electionsViewModel.selectFilter(filterOption)

        //THEN - election List displays the Elections
        val resultElectionList = electionsViewModel.filteredElections.getOrAwaitValue()
        MatcherAssert.assertThat(resultElectionList.size, `is`(3))
        MatcherAssert.assertThat(resultElectionList.first().id, `is`(0))
    }

    @Test
    fun selectFilter_no_option_provided_displays_all_elections() {

        // WHEN - no filter option is provided
        electionsViewModel.selectFilter()

        // THEN - verify all elections are displayed by default
        val resultElectionList = electionsViewModel.filteredElections.getOrAwaitValue()

        MatcherAssert.assertThat(resultElectionList, `is`(not(emptyList())))
        MatcherAssert.assertThat(resultElectionList.size, `is`(3))
    }

    companion object{
        private const val ALL_ELECTIONS = 1
        private const val SAVED_ELECTIONS = 2

        private fun createSomeElections(): List<Election> {
            val localDate = Date(1220227200L * 1000)

            return List(3) {
                Election(
                    it, "Election $it", localDate,
                    Division("$it-division", "USA", "California")
                )
            }

        }
    }
}