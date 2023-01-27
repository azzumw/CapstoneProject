package politicalpreparedness.election

import androidx.appcompat.view.menu.ActionMenuItem
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.network.models.SavedElection
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import repository.FakeRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.politicalpreparedness.election.ElectionsFragmentDirections
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.mockito.Mockito.*
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

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun allElections_DisplayedInUi() {
        // GIVEN - some elections in the repository
        (repository as FakeRepository).addElections(createSomeElections())

        // WHEN - Elections fragment launched to display elections
        launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme)

        // THEN - All three elections are shown on the screen
        onView(withText("Election 0")).check(matches(isDisplayed()))
        onView(withText("Election 1")).check(matches(isDisplayed()))
        onView(withText("Election 2")).check(matches(isDisplayed()))
    }

    @Test
    fun savedElection0_is_displayedInUi() = runTest {

        // GIVEN - some elections
        (repository as FakeRepository).addElections(createSomeElections())

        //save election with Election ID 0 to the database
        repository.saveThisElection(SavedElection(0))

        //mocking ActionMenu Item to return saved_elections item.id
        val mockedMenuOption = mock(ActionMenuItem::class.java)
        `when`(mockedMenuOption.itemId).thenReturn(R.id.saved_elections)

        // WHEN - Elections fragment launched to display elections with menu option 2 (savedElections)
        launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme).onFragment {
            it.onOptionsItemSelected(mockedMenuOption)
        }

        // THEN - it only shows Saved Elections: Election 0
        onView(withText("Election 0")).check(matches(isDisplayed()))
    }

    @Test
    fun emptyListOfElections_displaysNoDataMessage() {

        // GIVEN - an empty list of elections
        (repository as FakeRepository).addElections(emptyList())

        // WHEN - ElectionFragment launches to display elections
        launchFragmentInContainer<ElectionsFragment>(null, R.style.AppTheme)

        // THEN - No Data Shown error message is displayed
        onView(withId(R.id.no_data_in_db_msg)).check(matches(isDisplayed()))
        onView(withText(R.string.no_data_available_msg)).check(matches(isDisplayed()))

    }

    @Test
    fun noElectionResponseFromApi_displays_noNetworkStatusImage_and_Message() {
        // GIVEN - when there is no list assigned to the Election
        // or there is no ElectionResponse

        // WHEN - Elections fragment is launched
        launchFragmentInContainer<ElectionsFragment>(null,R.style.AppTheme)

        // THEN - verify No Network Status Image (cloud) is displayed
        onView(withId(R.id.statusImage)).check(matches(isDisplayed()))
    }

    @Test
    fun click_election_zero_navigate_to_corresponding_voterInfoFragment() {
        // GIVEN - some elections in the repository
        (repository as FakeRepository).addElections(createSomeElections())


        val scenario = launchFragmentInContainer<ElectionsFragment>(null,R.style.AppTheme)

        val mockedNavController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!,mockedNavController)
        }

        // WHEN - we click on the first election
        onView(withId(R.id.upComingElectionsRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0,click())
        )

        // THEN - Verify that we navigate to the first election's VoterInfo screen
        verify(mockedNavController).navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(0,Division("0-division","USA", "California")))

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