package com.example.android.politicalpreparedness.repository

import android.os.SystemClock
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
import java.time.LocalDate
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
    fun repositoryGetElections_hasCorrectItemAtPositionOne() = runBlockingTest{
        //WHEN - get elections is called
        repository.getElections()

        //THEN - correct Election item is retrieved at position 0
        val electionAtPositionZero = repository.elections.getOrAwaitValue()

        assertThat(electionAtPositionZero[0].name, `is`("Election 0"))
    }

    @Test
    fun repository_getElections_hasCorrectItems() = runBlockingTest(){
        //GIVEN -
        val date = 1220227200L * 1000
        val election1 = Election(0,"Election 0", Date(date), Division("0-division","USA","California"))
        val election2 = Election(1,"Election 1", Date(date), Division("1-division","USA","California"))
        val election3 = Election(2,"Election 2", Date(date), Division("2-division","USA","California"))


        //WHEN - getElections() is called
        repository.getElections()

        //THEN - elections LiveData property has correct list items
        val electionsList = repository.elections.getOrAwaitValue()

        assertThat(electionsList, hasItems(election1,election2,election3))
    }
}