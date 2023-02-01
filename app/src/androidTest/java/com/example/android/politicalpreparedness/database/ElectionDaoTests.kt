package com.example.android.politicalpreparedness.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.network.models.SavedElection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.android.politicalpreparedness.util.MainCoroutineRule
import com.example.android.politicalpreparedness.util.createThreeElectionInstances
import com.example.android.politicalpreparedness.util.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ElectionDaoTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var database : ElectionDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ElectionDatabase::class.java)
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllElections() = runTest{
        // GIVEN - some elections in the database
        val elections = createThreeElectionInstances()

        database.electionDao.insertAllElections(elections)

        // WHEN - get all the elections from the database
        val result = database.electionDao.getAllElections().getOrAwaitValue()

        // THEN - verify the loaded data contains the expected election elements
        MatcherAssert.assertThat(result, `is`(elections))
        MatcherAssert.assertThat(result, hasItems(elections[0],elections[1],elections[2]))
        MatcherAssert.assertThat(result.size, `is`(3))
    }

    @Test
    fun getAnElection() = runTest {
        // GIVEN - some elections in the database
        val elections = createThreeElectionInstances()
        database.electionDao.insertAllElections(elections)

        // WHEN - getAnElection is called
        val result = database.electionDao.getAnElection(1).getOrAwaitValue()

        // THEN - result contains the given election id instance
        MatcherAssert.assertThat(result, `is`(elections[1]))
        MatcherAssert.assertThat(result.id, `is`(elections[1].id))
        MatcherAssert.assertThat(result.electionDay, `is`(elections[1].electionDay))
        MatcherAssert.assertThat(result.name, `is`(elections[1].name))
    }

    @Test
    fun clearElectionsFromElectionsTable() = runTest{
        // GIVEN - some elections in the database
        val elections = createThreeElectionInstances()
        database.electionDao.insertAllElections(elections)

        // WHEN - database is cleared
        database.electionDao.clear()

        // THEN - verify election_table is empty
        val result = database.electionDao.getAllElections().getOrAwaitValue()
        MatcherAssert.assertThat(result, `is`(emptyList()))
    }

    @Test
    fun saveAnElection() = runTest {
        // GIVEN - some elections in the database
        val elections = createThreeElectionInstances()
        database.electionDao.insertAllElections(elections)

        // WHEN - an election with ID 0 is saved
        val savedElection = SavedElection(elections[0].id)
        database.electionDao.saveElection(savedElection)

        // THEN - verify that the saved election is saved in the database
        val result = database.electionDao.getSavedElectionByElectionID(savedElection.savedElectionId).getOrAwaitValue()
        MatcherAssert.assertThat(result.savedElectionId, `is`(0))
    }

    @Test
    fun deleteSavedElection() = runTest{
        // GIVEN - some elections
        val elections = createThreeElectionInstances()
        database.electionDao.insertAllElections(elections)

       // save the election 1
        val savedElection = SavedElection(elections[1].id)
        database.electionDao.saveElection(savedElection)

        //  - verify it is in the database
        val savedElectionResult = database.electionDao.getSavedElectionByElectionID(savedElection.savedElectionId).getOrAwaitValue()
        MatcherAssert.assertThat(savedElectionResult, `is`(savedElection))

        // WHEN - delete election is called
        database.electionDao.deleteElection(savedElection)

        // TH£N - verify saved election is not present in the database
        val r = database.electionDao.getSavedElectionByElectionID(savedElection.savedElectionId).getOrAwaitValue()
        MatcherAssert.assertThat(r, `is`(nullValue()))
    }

    @Test
    fun getSavedElections() = runTest {
        // GIVEN - some elections in the database
        val elections = createThreeElectionInstances()
        database.electionDao.insertAllElections(elections)

        // WHEN - election 1 and 2 are saved
        database.electionDao.saveElection(SavedElection(elections[1].id))
        database.electionDao.saveElection(SavedElection(elections[2].id))

        // THEN - verify saved Elections list consist of elections 1 and 2
        val result =
            database.electionDao.getElectionAndSavedElection().asLiveData().getOrAwaitValue()

         val r = result.map {
             it.election
         }
        MatcherAssert.assertThat(r, hasItems(elections[1],elections[2]))
        MatcherAssert.assertThat(r.size, `is`(2))

    }
}