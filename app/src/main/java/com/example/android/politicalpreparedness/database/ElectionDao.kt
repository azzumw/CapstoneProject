package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionAndSavedElection
import com.example.android.politicalpreparedness.network.models.SavedElection
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    //Queries for ElectionTable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(elections: List<Election>)

    // Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    // Add select single election query
    @Query("select * from election_table where id = :id")
    fun getAnElection(id: Int): LiveData<Election>


    // Add clear query
    @Query("delete from election_table")
    suspend fun clearAllElectionsFromElectionsTable()

    @Query("delete from saved_election_table")
    suspend fun clearAllElectionsFromSavedElectionsTable()

    //Queries for Saved_Election_Table
    // Add insert query
    @Insert(entity = SavedElection::class)
    suspend fun saveElection(savedElection: SavedElection)

    // Add delete query
    @Delete(entity = SavedElection::class)
    suspend fun deleteElection(savedElection: SavedElection)

    @Query("select * from saved_election_table where saved_election_id= :electionId")
    fun getSavedElectionByElectionID(electionId: Int): LiveData<SavedElection>

    @Transaction
    @Query("SELECT * FROM election_table, saved_election_table WHERE id == saved_election_id")
    fun getElectionAndSavedElection(): Flow<List<ElectionAndSavedElection>>

}