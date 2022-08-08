package com.example.android.politicalpreparedness.repository

import data.FakeDataSource
import org.junit.Assert.*
import org.junit.Before

class TheRepositoryTests{


    private lateinit var localDataSource: FakeDataSource
    private lateinit var remoteDataSource : FakeDataSource
    //subject under test
    private lateinit var repository: TheRepository

    @Before
    fun setup(){
        localDataSource = FakeDataSource()
        remoteDataSource = FakeDataSource()

        repository = TheRepository(localDataSource,remoteDataSource)
    }

    fun test(){

    }

}