package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.models.Election
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

    private lateinit var  electionsList:List<Election>

    @Before
    fun setUp() {
        // GIVEN - getVoterInformationViewModel is called

        electionsList = createThreeElectionInstances()
        fakeRepository = FakeRepository(electionsList)
    }

    @Test
    fun `getVoterInformation emptyStateInfo setsLiveDataToNull`() = mainCoroutineRule.runBlockingTest {

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

    @Test
    fun `getVoterInformation unSuccessfulNetworkCall nullVoterInfoUrl setsLiveDataToNull`() {

        //WHEN - state is set to return null
        fakeRepository.optionResult = 0

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify voterLocationUrl livedata is null
        val responseVoterVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterVoterLocationUrl, `is`(nullValue()))

    }

    @Test
    fun `getVoterInformation successfulNetworkCall emptyVoterInfoUrl setsLiveDataToNull`() {
        //WHEN - state is set to return null
        fakeRepository.optionResult = 1

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify voterLocationUrl livedata is null
        val responseVoterVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterVoterLocationUrl, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall withVoterInfoUrl updatesVoterInfoUrlLiveData`() {
        //WHEN - state is set to return null
        fakeRepository.optionResult = 2

        // and viewModel is instantiated
        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify voterLocationUrl livedata has data
        val responseVoterVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterVoterLocationUrl, `is`(notNullValue()))
        MatcherAssert.assertThat(responseVoterVoterLocationUrl, `is`("http://www.voting-info.com/${electionsList[0].id}"))

    }
}