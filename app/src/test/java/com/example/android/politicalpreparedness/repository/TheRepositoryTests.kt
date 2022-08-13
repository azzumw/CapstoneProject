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
import kotlinx.coroutines.Dispatchers


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

}