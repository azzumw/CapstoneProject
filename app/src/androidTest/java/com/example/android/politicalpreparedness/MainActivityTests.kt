package com.example.android.politicalpreparedness

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.example.android.politicalpreparedness.repository.FakeRepository
import com.example.android.politicalpreparedness.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTests {


    companion object {
        private lateinit var uiDevice: UiDevice
        //waiting times
        private const val ONE_SEC = 1000L

        //components
        private val upComingElectionsButton: ViewInteraction =
            onView(withId(R.id.upComingElectionsButton))

        private val representativeButton : ViewInteraction =
            onView(withId(R.id.findRepresentativeButton))

        private val electionsRecyclerView: ViewInteraction =
            onView(withId(R.id.upComingElectionsRecyclerView))

        //menu items
        private val allElectionsMenuItem = onView(withText("All Elections"))
        private val savedElectionsMenuItem = onView(withText("Saved Elections"))

        //votersFragment components
        private val followOrUnFollowButton = onView(withId(R.id.saveElectionButton))

        //representative components
        private  val representativeTitle :ViewInteraction = onView(withText("Representative Search"))

        //functions
        private fun ViewInteraction.click(): ViewInteraction = this.perform(ViewActions.click())
        private fun ViewInteraction.isDisplayed(): ViewInteraction =
            this.check(matches(ViewMatchers.isDisplayed()))

        //packages for screens
        private const val voterInfoFragmentPackage = "com.example.android.politicalpreparedness.election.VoterInfoFragment"
        private const val electionsFragmentPackage = "com.example.android.politicalpreparedness.election.ElectionsFragment"
        private const val representativePackage = "com.example.android.politicalpreparedness.representative.RepresentativeFragment"

        @BeforeClass
        @JvmStatic
        fun setDevicePreferences(){
            uiDevice = UiDevice.getInstance(getInstrumentation())
            uiDevice.executeShellCommand(ANIMATION_OFF)
            uiDevice.executeShellCommand(TRANS_ANIMATION_OFF)
            uiDevice.executeShellCommand(WIN_ANIMATION_OFF)
        }
    }


    private lateinit var repository: FakeRepository

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Before
    fun setUp() {

        ServiceLocator.provideRepository(ApplicationProvider.getApplicationContext())
//        repository = FakeRepository()
//        ServiceLocator.repository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun navigateToElectionsScreen() {
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        onView(withId(R.id.upComingElectionsButton)).perform(click())


        electionsRecyclerView.isDisplayed()

        scenario.close()
    }

    @Test
    fun navigateToRepresentativesScreen() {
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        representativeButton.click()

        representativeTitle.isDisplayed()

        scenario.close()
    }

    @Test
    fun saveAnElection_displaysInSavedElections() {

        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        upComingElectionsButton.click()

        electionsRecyclerView
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                    0,
                    click()
                )
            )

        uiDevice.waitForWindowUpdate(voterInfoFragmentPackage, ONE_SEC)

        onView(withText("VIP Test Election")).isDisplayed()

        followOrUnFollowButton.isDisplayed()
        followOrUnFollowButton.click()

        pressBack()

        electionsRecyclerView.isDisplayed()

        uiDevice.pressMenu()

        savedElectionsMenuItem.click()

        onView(withText("VIP Test Election")).isDisplayed()

        scenario.close()

    }
}