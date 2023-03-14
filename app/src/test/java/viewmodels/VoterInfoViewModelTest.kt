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
import org.junit.Ignore
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
        val responseVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterLocationUrl, `is`(nullValue()))

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
        val responseVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterLocationUrl, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall withVoterInfoUrl updatesVoterInfoUrlLiveData`() {
        //WHEN - state is set to return data
        fakeRepository.optionResult = 2

        // and viewModel is instantiated
        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify voterLocationUrl livedata has data
        val responseVoterLocationUrl = voterInfoViewModel.voterLocationUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseVoterLocationUrl, `is`(notNullValue()))
        MatcherAssert.assertThat(responseVoterLocationUrl, `is`("http://www.voting-info.com/${electionsList[0].id}"))

    }

    @Test
    fun `getVoterInformation unSuccessfulNetworkCall nullBallotInfoUrl setsLiveDataToNull`() {
        //WHEN - state is set to return null
        fakeRepository.optionResult = 0

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify ballotLocationUrl livedata is null
        val responseBallotInfoUrl = voterInfoViewModel.ballotInfoUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseBallotInfoUrl, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall emptyBallotInfoUrl setsLiveDataToNull`() {
        //WHEN - state is set to return empty
        fakeRepository.optionResult = 1

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify ballotLocationUrl livedata is null
        val responseBallotInfoUrl = voterInfoViewModel.ballotInfoUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseBallotInfoUrl, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall withBallotInfoUrl updatesBallotInfoUrlLiveData`() {
        //WHEN - state is set to return data
        fakeRepository.optionResult = 2

        // and viewModel is instantiated
        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify voterLocationUrl livedata has data
        val responseBallotInfoUrl = voterInfoViewModel.ballotInfoUrl.getOrAwaitValue()
        MatcherAssert.assertThat(responseBallotInfoUrl, `is`(notNullValue()))
        MatcherAssert.assertThat(responseBallotInfoUrl, `is`("http://www.ballotinfo.com/${electionsList[0].id}"))

    }

    @Test
    fun `getVoterInformation unSuccessfulNetworkCall updates isVoterAndBallotInfoNull toTrue`() {
        //WHEN - state is set to return null
        fakeRepository.optionResult = 0

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify isVoterAndBallotInfoNull is true
        val responseIsVoterAndBallotInfoNull = voterInfoViewModel.isVoterAndBallotInfoNull.getOrAwaitValue()
        MatcherAssert.assertThat(responseIsVoterAndBallotInfoNull, `is`(true))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall withEmptyData updates isVoterAndBallotInfoNull toTrue`() {
        //WHEN - state is set to return emptyList
        fakeRepository.optionResult = 1

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify isVoterAndBallotInfoNull is true
        val responseIsVoterAndBallotInfoNull = voterInfoViewModel.isVoterAndBallotInfoNull.getOrAwaitValue()
        MatcherAssert.assertThat(responseIsVoterAndBallotInfoNull, `is`(true))

    }

    @Test
    fun `getVoterInformation successfulNetworkCall hasData updates isVoterAndBallotInfoNull toFalse`(){
        //WHEN - state is set to return data
        fakeRepository.optionResult = 2

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify isVoterAndBallotInfoNull is false
        val result = voterInfoViewModel.isVoterAndBallotInfoNull.getOrAwaitValue()
        MatcherAssert.assertThat(result, `is`(false))
    }

    @Test
    fun `getVoterInformation unsuccessfulNetworkCall  sets correspondenceAddressLiveData to Null`() {
        //WHEN - state is set to return null
        fakeRepository.optionResult = 0

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify correspondenceAddress is null
        val result = voterInfoViewModel.correspondenceAddress.getOrAwaitValue()
        MatcherAssert.assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall  withEmptyData sets correspondenceAddressLiveData to Null`() {
        //WHEN - state is set to return empty list
        fakeRepository.optionResult = 1

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify correspondenceAddress is null
        val result = voterInfoViewModel.correspondenceAddress.getOrAwaitValue()
        MatcherAssert.assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `getVoterInformation successfulNetworkCall hasData updates correspondenceAddressLiveData`() {
        //WHEN - state is set to return data
        fakeRepository.optionResult = 2

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        // THEN - verify correspondenceAddress has data
        val result = voterInfoViewModel.correspondenceAddress.getOrAwaitValue()
        MatcherAssert.assertThat(result, `is`(notNullValue()))
        MatcherAssert.assertThat(result?.state, `is`("Baja California"))
        MatcherAssert.assertThat(result?.city, `is`("Tijuana"))
    }

    @Test
    @Ignore("work in progress...")
    fun `getVoterInformation unsuccessfulNetworkCall  sets electionLiveData to Null`() {
        //WHEN - state is set to return data

        voterInfoViewModel = VoterInfoViewModel(
            fakeRepository, electionsList[0].id,
            electionsList[0].division
        )

        val electionResult = voterInfoViewModel.election.getOrAwaitValue()
        MatcherAssert.assertThat(electionResult, `is`(notNullValue()))
    }
}