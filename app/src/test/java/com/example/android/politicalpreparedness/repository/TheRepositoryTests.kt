package com.example.android.politicalpreparedness.repository

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
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
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
class TheRepositoryTests {

    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource: FakeDataSource

    //subject under test
    private lateinit var repository: TheRepository

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
    fun getElections_returnsTheCorrectSize() = runTest {
        //WHEN - get elections is called
        repository.getElections()

        //THEN - correct elections list size is returned
        val value = repository.getElectionsFromLocalDataBase().getOrAwaitValue()
        MatcherAssert.assertThat(value.size, `is`(3))
        MatcherAssert.assertThat(value[0].id, `is`(0))
        MatcherAssert.assertThat(value[0].name, `is`("Election 0"))

    }

    @Test
    fun getAnElection_returnsCorrectElection() = runTest{

        //GIVEN - list of elections is present
        repository.getElections()

        //WHEN - an election is retrieved with id 0
        val election = repository.getAnElection(0)

        //THEN - correct election instance is returned with the correct name.
        election.getOrAwaitValue()
        MatcherAssert.assertThat(election.value?.name, `is`("Election 0"))
    }

    @Test
    fun saveThisElection_returnsTheSavedElection()= runTest{
        // GIVEN - an election to be saved
        repository.getElections()
        val savedElection = SavedElection(0)
        // WHEN - this election is saved
        repository.saveThisElection(savedElection)

        // THEN - verify it is in the savedElections
        val value = repository.getElectionIdFromSavedElection(savedElection.savedElectionId).getOrAwaitValue()

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
        val savedElectionValue = repository.getElectionIdFromSavedElection(savedElection.savedElectionId).getOrAwaitValue()
        val savedElectionValue2 = repository.getElectionIdFromSavedElection(savedElection2.savedElectionId).getOrAwaitValue()
        MatcherAssert.assertThat(savedElectionValue.savedElectionId, `is`(1))
        MatcherAssert.assertThat(savedElectionValue2.savedElectionId, `is`(2))

        // WHEN - savedElection with ID 1 is removed
        repository.removeThisElection(savedElection)

        // THEN - verify only savedElection with ID 1 is removed
        val result = repository.getElectionIdFromSavedElection(savedElection.savedElectionId).getOrAwaitValue()
        MatcherAssert.assertThat(result, nullValue())
        val result2 = repository.getElectionIdFromSavedElection(savedElection2.savedElectionId).getOrAwaitValue ()
        MatcherAssert.assertThat(result2, notNullValue())
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