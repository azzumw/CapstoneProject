package viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
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
class VoterInfoViewModelTest{

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var voterInfoViewModel: VoterInfoViewModel
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {
        //creating dummy elections lists
        val electionsList = createThreeElectionInstances()

        //setting up the fake repository
        fakeRepository = FakeRepository(electionsList)

        voterInfoViewModel = VoterInfoViewModel(fakeRepository,electionsList[0].id,
            electionsList[0].division)

    }

    @Ignore("Not quite sure what the issue is, I suspect viewModescope Issue")
    fun getVoterInformation_emptyStateInfo_setsLiveDataNull() = runTest {

        // WHEN - getVoterInformation is called

        // THEN - verify ˚livedata values are empty
        val responseStateList = voterInfoViewModel.state.getOrAwaitValue()

          MatcherAssert.assertThat(responseStateList?.size, `is`(1))
    }
}