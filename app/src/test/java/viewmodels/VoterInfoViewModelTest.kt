package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import repository.FakeRepository
import util.MainCoroutineRule
import util.createThreeElectionInstances
import util.getOrAwaitValue
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class VoterInfoViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var voterInfoViewModel: VoterInfoViewModel
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {

    }

    @Test
    fun `getVoterInformation emptyStateInfo setsLiveDataToNull`() = mainCoroutineRule.runBlockingTest {

        // GIVEN - getVoterInformationViewModel is called
        //creating dummy elections lists
        val electionsList = createThreeElectionInstances()

        //setting up the fake repository
        fakeRepository = FakeRepository(electionsList)

        //WHEN - state is set to null
        fakeRepository.optionResult = 1

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )


        // THEN - verify ˚livedata values are empty
        val responseStateList = voterInfoViewModel.state.getOrAwaitValue()
        MatcherAssert.assertThat(responseStateList, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation nullStateInfo setsLiveDataToNull`() {
        // GIVEN - getVoterInformationViewModel is called
        //creating dummy elections lists
        val electionsList = createThreeElectionInstances()

        //setting up the fake repository
        fakeRepository = FakeRepository(electionsList)

        //WHEN - state is set to null
        fakeRepository.optionResult = 0

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify ˚livedata values are empty
        val responseStateList = voterInfoViewModel.state.getOrAwaitValue()
        MatcherAssert.assertThat(responseStateList, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation withStateInfo updatesStatesListLiveData`() {
        // GIVEN - getVoterInformationViewModel is called
        //creating dummy elections lists
        val electionsList = createThreeElectionInstances()

        //setting up the fake repository
        fakeRepository = FakeRepository(electionsList)

        //WHEN - state is set to return results
        fakeRepository.optionResult = 2

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify ˚livedata values have data
        val responseStateList = voterInfoViewModel.state.getOrAwaitValue()
        MatcherAssert.assertThat(responseStateList, `is`(notNullValue()))
        MatcherAssert.assertThat(responseStateList?.size, `is`(3))
    }
}