package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.network.models.State

@Database(entities = [State::class,Election::class,SavedElection::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase: RoomDatabase() {

    abstract val electionDao: ElectionDao

    companion object {

        @Volatile
        private var INSTANCE: ElectionDatabase? = null

        fun getInstance(context: Context): ElectionDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ElectionDatabase::class.java,
                            "election_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}