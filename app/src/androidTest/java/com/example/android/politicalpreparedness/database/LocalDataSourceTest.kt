    package com.example.android.politicalpreparedness.database

    import android.content.Context
    import androidx.arch.core.executor.testing.InstantTaskExecutorRule
    import androidx.room.Room
    import androidx.test.core.app.ApplicationProvider
    import androidx.test.ext.junit.runners.AndroidJUnit4
    import androidx.test.filters.MediumTest
    import com.example.android.politicalpreparedness.network.models.SavedElection
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.test.runTest
    import org.hamcrest.CoreMatchers.*
    import org.hamcrest.MatcherAssert
    import org.junit.After
    import org.junit.Assert.*
    import org.junit.Before
    import org.junit.Rule
    import org.junit.Test
    import org.junit.runner.RunWith
    import util.createThreeElectionInstances
    import util.getOrAwaitValue

    @MediumTest
    @RunWith(AndroidJUnit4::class)
    @ExperimentalCoroutinesApi
    class LocalDataSourceTest {

        @get:Rule
        var rule = InstantTaskExecutorRule()

        //subjects under test
        private lateinit var localDataSource: LocalDataSource
        private lateinit var database: ElectionDatabase

        @Before
        fun setUp() {
            database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext<Context?>()
                    .applicationContext, ElectionDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

            localDataSource = LocalDataSource(database.electionDao)
        }

        @After
        fun tearDown() {
            //clean database
            database.close()
        }

        @Test
        fun insertElections_savesElectionsInTheDatabase() = runTest {
            // GIVEN - some elections
            val elections = createThreeElectionInstances()

            // WHEN - insertElections() is invoked
            localDataSource.insertElections(elections)

            // THEN - verify elections list is stored in the database
            val result = localDataSource.getElections().getOrAwaitValue()
            MatcherAssert.assertThat(result, `is`(elections))
            MatcherAssert.assertThat(result.size, `is`(3))
            MatcherAssert.assertThat(result, hasItems(elections[0], elections[1], elections[2]))


        }

        @Test
        fun getSavedElections() = runTest {
             // GIVEN -  some elections
            val elections =  createThreeElectionInstances()
            localDataSource.insertElections(elections)

            val savedElection1 = SavedElection(elections.first().id)
            val savedElection2 = SavedElection(elections.last().id)

            //save first and last election
            localDataSource.saveThisElection(savedElection1)
            localDataSource.saveThisElection(savedElection2)

            // WHEN - two saved elections are retrieved
            val savedElections = localDataSource.getSavedElections().getOrAwaitValue().map {
                it.election
            }

            // THEN - verify the two saved elections match the ones saved.
            MatcherAssert.assertThat(savedElections.size, `is`(2))
            MatcherAssert.assertThat(savedElections.first(), `is`(elections.first()))
            MatcherAssert.assertThat(savedElections.last(), `is`(elections.last()))
            MatcherAssert.assertThat(savedElections.first().id, `is`(savedElection1.savedElectionId))
            MatcherAssert.assertThat(savedElections.last().id, `is`(savedElection2.savedElectionId))


        }

        @Test
        fun getElections() = runTest {
            // GIVEN - some elections
            val elections = createThreeElectionInstances()

            // WHEN - insertElections() is invoked
            localDataSource.insertElections(elections)

            // THEN - verify elections list is stored in the database
            val result = localDataSource.getElections().getOrAwaitValue()
            MatcherAssert.assertThat(result, `is`(elections))
            MatcherAssert.assertThat(result.size, `is`(3))
            MatcherAssert.assertThat(result, hasItems(elections[0], elections[1], elections[2]))
        }

        @Test
        fun getAnElection() = runTest {
            // GIVEN - some elections
            val elections = createThreeElectionInstances()
            localDataSource.insertElections(elections)

            // WHEN - election at index 2 is retrieved
            val result = localDataSource.getAnElection(elections[2].id).getOrAwaitValue()

            // THEN - verify it returns the same election instance
            MatcherAssert.assertThat(result, `is`(elections[2]))
            MatcherAssert.assertThat(result.id, `is`(elections[2].id))
        }

        @Test
        fun saveThisElection() = runTest {
            // GIVEN - an election
            val election = createThreeElectionInstances()[0]

            // WHEN - the election is saved in the database
            localDataSource.saveThisElection(SavedElection(election.id))

            // THEN - verify it is saved in the database
            val result = localDataSource.getSavedElectionByElectionID(election.id).getOrAwaitValue()
            MatcherAssert.assertThat(result.savedElectionId, `is`(election.id))

        }

        @Test
        fun removeOrUnFollowThisElection() = runTest {
            // GIVEN - an election is saved/followed
            val election = createThreeElectionInstances()[0]
            localDataSource.saveThisElection(SavedElection(election.id))

            val savedElection =
                localDataSource.getSavedElectionByElectionID(election.id).getOrAwaitValue()
            //check it is saved:
            MatcherAssert.assertThat(savedElection.savedElectionId, `is`(election.id))

            // WHEN - savedElection is removed
            localDataSource.removeThisElection(savedElection)

            // THEN - verify this election is removed from the saved elections table
            val result = localDataSource.getSavedElectionByElectionID(election.id).getOrAwaitValue()
            MatcherAssert.assertThat(result, `is`(nullValue()))

        }

        @Test
        fun removeOrUnFollow_UnfollowsTheCorrectSavedElection() = runTest{
            // GIVEN - some elections
            val elections = createThreeElectionInstances()
            localDataSource.insertElections(elections)

            val savedElection1 = SavedElection(elections[1].id)
            val savedElection2 = SavedElection(elections[2].id)

            //save last two elections
            localDataSource.saveThisElection(savedElection1)
            localDataSource.saveThisElection(savedElection2)

            //check both elections are stored in the SavedElections table
            val savedElections = localDataSource.getSavedElections().getOrAwaitValue()
            MatcherAssert.assertThat(savedElections.size, `is`(2))

            // WHEN - election[2] is unfollowed or removed
            localDataSource.removeThisElection(savedElection2)

            // THEN - verify election[2] is removed and not election[1]
            val savedElectionsUpdated = localDataSource.getSavedElections().getOrAwaitValue()
            val savedElection2RemovedResult = localDataSource.getSavedElectionByElectionID(savedElection2.savedElectionId).getOrAwaitValue()
            val savedElection1NotRemovedResult = localDataSource.getSavedElectionByElectionID(savedElection1.savedElectionId).getOrAwaitValue()
            MatcherAssert.assertThat(savedElection2RemovedResult, `is`(nullValue()))
            MatcherAssert.assertThat(savedElection1NotRemovedResult, `is`(savedElection1))
            MatcherAssert.assertThat(savedElectionsUpdated.size, `is`(1))

        }

        @Test
        fun getSavedElectionByElectionID()  = runTest{
            // GIVEN - an election in the database
            val election = createThreeElectionInstances()
            localDataSource.insertElections(election)
            localDataSource.saveThisElection(SavedElection(election[0].id))

            // WHEN - saved Election is retrieved by the election ID
            val savedElection = localDataSource.getSavedElectionByElectionID(election[0].id).getOrAwaitValue()
            val listSavedElections = localDataSource.getSavedElections().getOrAwaitValue().map {
                it.election
            }
            // THEN - verify it is the same election
            MatcherAssert.assertThat(savedElection.savedElectionId, `is`(election[0].id))
            MatcherAssert.assertThat(listSavedElections.size, `is`(1))
            MatcherAssert.assertThat(listSavedElections[0], `is`(election[0]))
        }

    }