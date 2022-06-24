package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(elections:List<Election>)

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    // Add select all election query
    @Query("select * from election_table")
    fun getAllElections():LiveData<List<Election>>

    // Add select single election query
    fun getAnElection():LiveData<Election>

    // Add delete query
    @Delete
    fun deleteElection(election: Election)

    // Add clear query
    @Query("delete from election_table")
    fun clear()
}