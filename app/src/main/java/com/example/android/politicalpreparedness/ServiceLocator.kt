package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import com.example.android.politicalpreparedness.repository.TheRepository

object ServiceLocator {

    private val lock = Any()

    //1. create a repository variable
    @Volatile
    var repository: RepositoryInterface? = null
        @VisibleForTesting set

    private var database: ElectionDatabase? = null

    fun provideRepository(context: Context): RepositoryInterface {
        synchronized(this) {
            return repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): RepositoryInterface {
        val newRepo = TheRepository(createLocalDataSource(context), RemoteDataSource)
        repository = newRepo

        return newRepo
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val db = database ?: createDatabase(context)

        return LocalDataSource(db.electionDao)
    }

    private fun createDatabase(context: Context): ElectionDatabase {
        val result = ElectionDatabase.getInstance(context)
        database = result
        return result

    }

    @VisibleForTesting
    fun resetRepository() {

        synchronized(lock) {
//            runBlocking {
//                LocalDataSource().clear()
//
//            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null

            repository = null
        }
    }
}