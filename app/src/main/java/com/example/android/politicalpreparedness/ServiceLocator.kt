package com.example.android.politicalpreparedness

import android.content.Context
import androidx.room.Room
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.repository.DataSourceInterface
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import com.example.android.politicalpreparedness.repository.TheRepository

object ServiceLocator {

    private var database: ElectionDatabase? = null
    @Volatile
    var tasksRepository: RepositoryInterface? = null

    fun provideTasksRepository(context: Context): RepositoryInterface {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): RepositoryInterface {
        val newRepo = TheRepository(remoteDataSource = RemoteDataSource, localDataSource = createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): DataSourceInterface {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.electionDao)
    }

    private fun createDataBase(context: Context): ElectionDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ElectionDatabase::class.java, "election_database"
        ).build()
        database = result
        return result
    }
}