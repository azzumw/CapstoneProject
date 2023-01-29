package database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.database.ElectionDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import util.createSomeElections
import util.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ElectionDaoTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : ElectionDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ElectionDatabase::class.java)
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllElections() = runTest{
        // GIVEN - some elections in the database
        val elections = createSomeElections()

        database.electionDao.insertAllElections(elections)

        // WHEN - get all the elections from the database
        val result = database.electionDao.getAllElections().getOrAwaitValue()

        // THEN - verify the loaded data contains the expected election elements
        MatcherAssert.assertThat(result, `is`(elections))
        MatcherAssert.assertThat(result, hasItems(elections[0],elections[1],elections[2]))
        MatcherAssert.assertThat(result.size, `is`(3))
    }


}