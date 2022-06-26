package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(elections:List<Election>)

//    // Add insert query
//    @Insert(entity = SavedElection::class, onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveElection(election: Election)

    // Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    // Add select single election query
    @Query("select * from election_table where id = :id")
    fun getAnElection(id:Int): Flow<Election>

    // Add delete query
    @Delete
    suspend fun deleteElection(election: Election)

    // Add clear query
    @Query("delete from election_table")
    suspend fun clear()
}