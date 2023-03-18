package repository

import data.FakeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import util.getOrAwaitValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.TheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.junit.After
import util.MainCoroutineRule
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TheRepositoryTests {

    //subject under test
    private lateinit var repository: TheRepository

    //dependencies
    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        val electionList = createThreeElectionInstances().toMutableList()
        localDataSource = FakeDataSource(electionList)
        remoteDataSource = localDataSource

        repository = TheRepository(localDataSource, remoteDataSource, Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        runBlocking {
            repository.deleteAllElections()
            repository.deleteAllSavedElections()
        }
    }

    @Test
    fun getElections_insertsTheElections() = runTest {
        //WHEN - get elections is called
        repository.getElections()

        //THEN - correct elections list size is returned
        val value = repository.getElectionsFromLocalDataBase().getOrAwaitValue()
        MatcherAssert.assertThat(value.size, `is`(3))
        MatcherAssert.assertThat(value[0].id, `is`(0))
        MatcherAssert.assertThat(value[0].name, `is`("Election 0"))

    }

    @Test
    fun getAnElection_returnsCorrectElection() = runTest {

        //GIVEN - list of elections is present
        repository.getElections()

        //WHEN - an election is retrieved with id 0
        val election = repository.getAnElection(0)

        //THEN - correct election instance is returned with the correct name.
        election.getOrAwaitValue()
        MatcherAssert.assertThat(election.value?.name, `is`("Election 0"))
    }

    @Test
    fun saveThisElection_returnsTheSavedElection() = runTest {

        // GIVEN - an election to be saved
        repository.getElections()
        val savedElection = SavedElection(0)

        // WHEN - this election is saved
        repository.saveThisElection(savedElection)

        // THEN - verify it is in the savedElections
        val value = repository.getSavedElectionByElectionID(savedElection.savedElectionId)
            .getOrAwaitValue()

        MatcherAssert.assertThat(value.savedElectionId, `is`(0))
        MatcherAssert.assertThat(value.savedElectionId, `is`(not(1)))

    }

    @Test
    fun removeThisElection_twoSavedElections_removesElectionOne() = runTest {

        // GIVEN  - three elections, and two of the elections are saved
        repository.getElections()
        val savedElection = SavedElection(1)
        val savedElection2 = SavedElection(2)
        repository.saveThisElection(savedElection)
        repository.saveThisElection(savedElection2)

        // verify they are stored in repository
        val savedElectionValue =
            repository.getSavedElectionByElectionID(savedElection.savedElectionId)
                .getOrAwaitValue()
        val savedElectionValue2 =
            repository.getSavedElectionByElectionID(savedElection2.savedElectionId)
                .getOrAwaitValue()
        MatcherAssert.assertThat(savedElectionValue.savedElectionId, `is`(1))
        MatcherAssert.assertThat(savedElectionValue2.savedElectionId, `is`(2))

        // WHEN - savedElection with ID 1 is removed
        repository.removeThisElection(savedElection)

        // THEN - verify only savedElection with ID 1 is removed, and SavedElection with ID 2 is retained
        val result = repository.getSavedElectionByElectionID(savedElection.savedElectionId)
            .getOrAwaitValue()
        val result2 = repository.getSavedElectionByElectionID(savedElection2.savedElectionId)
            .getOrAwaitValue()

        MatcherAssert.assertThat(result, nullValue())
        MatcherAssert.assertThat(result2, notNullValue())
    }

    @Test
    fun callVoterInfoApi_returns_voterInfoResponse() = runTest {

        // GIVEN - some elections in the repository
        localDataSource.insertElections(createThreeElectionInstances())

        // WHEN - call to voterInfoApi is made with electionID = 1
        val electionId = 1
        val response = repository.callVoterInfoApi("address", electionId.toString())

        // THEN - verify the response return as VoterInfoResponse contains the same Election instance
        val resultElection = localDataSource.getAnElection(1).getOrAwaitValue()
        MatcherAssert.assertThat(response?.election, `is`(resultElection))
    }

    @Test
    fun callRepresentativeInfoApi_returns_correctRepresentativeResponse() = runTest {
        // GIVEN - when an address is provided
        val address = Address("10 Downing St", city = "London", state = "London", zip = "10089")

        // WHEN - representativeInfoApi is called with the given address
        val response = localDataSource.callRepresentativeInfoApi(address)

        // THEN - verify it returns the expected official/office data
        MatcherAssert.assertThat(response.offices[0].name, `is`("House Of Commons"))
        MatcherAssert.assertThat(response.officials[0].name, `is`("Rishi Sunak"))

    }

    @Test
    fun getSavedElectionsFromLocalDatabase() = runTest {
        // GIVEN  - three elections, and two of the elections are saved
        repository.getElections()
        val savedElection = SavedElection(1)
        val savedElection2 = SavedElection(2)
        repository.saveThisElection(savedElection)
        repository.saveThisElection(savedElection2)

        // WHEN - getSavedElectionsFromLocalDatabase is invoked
        val savedElectionsResponse = repository.getSavedElectionsFromLocalDataSource().getOrAwaitValue()

        // THEN - verify that the savedElections are stored in the SavedElection list
        val savedElectionsList = savedElectionsResponse.map {
            it.savedElection
        }

        MatcherAssert.assertThat(savedElectionsList, hasItems(savedElection,savedElection2))
        MatcherAssert.assertThat(savedElectionsList, hasItems(not(SavedElection(0))))
    }

    private fun createThreeElectionInstances(): List<Election> {
        val localDate = Date(1220227200L * 1000)

        return List(3) {
            Election(
                it, "Election $it", localDate,
                Division("$it-division", "USA", "California")
            )
        }
    }
}