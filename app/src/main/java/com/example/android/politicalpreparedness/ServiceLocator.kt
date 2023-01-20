package com.example.android.politicalpreparedness

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import com.example.android.politicalpreparedness.repository.TheRepository

object ServiceLocator {

    //1. create a repository variable
    @Volatile
    var repository : RepositoryInterface? = null

    private var database:ElectionDao? = null

    fun provideRepository(context: Context) : RepositoryInterface{
        synchronized(this){
            return repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context):RepositoryInterface{
        val newRepo = TheRepository(createLocalDataSource(context),RemoteDataSource)
        repository = newRepo

        return newRepo
    }

    private fun createLocalDataSource(context: Context):LocalDataSource{
        val database = database ?: createDatabase(context).electionDao
        return LocalDataSource(database)
    }

    private fun createDatabase(context: Context):ElectionDatabase{
        return ElectionDatabase.getInstance(context)
    }
}