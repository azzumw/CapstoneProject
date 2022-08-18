package com.example.android.politicalpreparedness.repository

import data.FakeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
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
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.hasItems
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
        localDataSource = FakeDataSource()
        remoteDataSource = FakeDataSource()

        repository = TheRepository(localDataSource, remoteDataSource, Dispatchers.Unconfined)
    }

    @Test
    fun repositoryGetElections_returnsTheCorrectSize() = runBlockingTest {
        //WHEN - get elections is called
        repository.getElections()

        //THEN - correct elections list size is returned
        val value = repository.elections.getOrAwaitValue()
        assertThat(value.size, `is`(3))

    }

    @Test
    fun getElections_hasCorrectItemAtPositionOne() = runBlockingTest{
        //WHEN - get elections is called
        repository.getElections()

        //THEN - correct Election item is retrieved at position 0
        val electionAtPositionZero = repository.elections.getOrAwaitValue()

        assertThat(electionAtPositionZero[0].name, `is`("Election 0"))
    }

    @Test
    fun getElections_hasCorrectElectionItems() = runBlockingTest{
        //GIVEN - some Election instances
        val date = 1220227200L * 1000
        val election1 = Election(0,"Election 0", Date(date), Division("0-division","USA","California"))
        val election2 = Election(1,"Election 1", Date(date), Division("1-division","USA","California"))
        val election3 = Election(2,"Election 2", Date(date), Division("2-division","USA","California"))

        //WHEN - getElections() is called
        repository.getElections()

        //THEN - elections LiveData property has correct list items matching given Election instances
        val electionsList = repository.elections.getOrAwaitValue()

        assertThat(electionsList, hasItems(election1,election2,election3))
    }

    @Test
    fun getAnElection_returnsCorrectElection() = runBlockingTest{

        //GIVEN - list of elections is present
        repository.getElections()

        //WHEN - an election is retrieved with id 0
        val election = repository.getAnElection(0)

        //THEN - correct election instance is returned with the correct name.
        election.getOrAwaitValue()
        assertThat(election.value?.name, `is`("Election 0"))
    }

    fun saveThisElection_returnsTheSavedElection(){
        //not able to implement
    }


}