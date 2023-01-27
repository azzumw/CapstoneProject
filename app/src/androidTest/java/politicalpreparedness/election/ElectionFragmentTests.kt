package politicalpreparedness.election


import android.os.SystemClock
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import repository.FakeRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.viewModels
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Ignore
import java.util.*

/*
* Important! Issue with fragment testing on latest libs
* Current working lib version downgraded to 1.3.0-alpha08
* https://issuetracker.google.com/issues/128612536#comment22
* */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class ElectionFragmentTests {

    private lateinit var repository: RepositoryInterface

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun allElections_DisplayedInUi() {
        // GIVEN - some elections in the repository
        repository = FakeRepository(createSomeElections())
        ServiceLocator.repository = repository

        // WHEN - Elections fragment launched to display elections
        launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme)

        // THEN - All three elections are shown on the screen
        onView(withText("Election 0")).check(matches(isDisplayed()))
        onView(withText("Election 1")).check(matches(isDisplayed()))
        onView(withText("Election 2")).check(matches(isDisplayed()))
    }


    @Test
    @Ignore
    fun savedElections_DisplayedInUi() = runTest {

        repository = FakeRepository(createSomeElections())
        ServiceLocator.repository = repository
        // GIVEN - add saved election to the database
        repository.saveThisElection(SavedElection(0))

        // WHEN - Elections fragment launched to display elections

        val context = InstrumentationRegistry.getInstrumentation().context
        val addMenuItem = ActionMenuItem(context, 0, R.id.saved_elections, 0, 0, "Saved Elections")
        val f = launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme)

        f.onFragment {

            it.onOptionsItemSelected(addMenuItem)

        }
    }

    @Test
    fun emptyListOfElections_displaysNoDataMessage() {

        // GIVEN - an empty list of elections
        repository = FakeRepository()
        ServiceLocator.repository = repository

        // WHEN - ElectionFragment launches to display elections
        launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme)

        onView(withId(R.id.no_data_in_db_msg)).check(matches(isDisplayed()))
        onView(withText(R.string.no_data_available_msg)).check(matches(isDisplayed()))

    }

    private fun createSomeElections(): List<Election> {
        val localDate = Date(1220227200L * 1000)

        return List(3) {
            Election(
                it, "Election $it", localDate,
                Division("$it-division", "USA", "California")
            )
        }
    }
}