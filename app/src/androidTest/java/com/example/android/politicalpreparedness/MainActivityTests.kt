package com.example.android.politicalpreparedness

import android.app.Instrumentation
import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.UriMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import com.example.android.politicalpreparedness.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
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
        private val navToUpComingElectionsButton: ViewInteraction =
            onView(withId(R.id.upComingElectionsButton))

        private val navToRepresentativeScreenButton: ViewInteraction =
            onView(withId(R.id.findRepresentativeButton))

        private val electionsRecyclerView: ViewInteraction =
            onView(withId(R.id.upComingElectionsRecyclerView))

        //menu items
        private val allElectionsMenuItem = onView(withText("All Elections"))
        private val savedElectionsMenuItem = onView(withText("Saved Elections"))

        //votersFragment components
        private val followOrUnFollowButton = onView(withId(R.id.saveElectionButton))

        //representative components
        private val representativeTitle: ViewInteraction = onView(withText("Representative Search"))
        private val findMyRepresentativesButton: ViewInteraction =
            onView(withId(R.id.find_my_representatives_button))

        private val useMyLocationButton: ViewInteraction = onView(withId(R.id.button_location))
        private val representativesRecyclerView: ViewInteraction =
            onView(withId(R.id.representative_recycler))

        //dsl functions
        private fun ViewInteraction.click(): ViewInteraction = this.perform(ViewActions.click())
        private fun ViewInteraction.isDisplayed(): ViewInteraction =
            this.check(matches(ViewMatchers.isDisplayed()))

        //packages for screens
        private const val voterInfoFragmentPackage =
            "com.example.android.politicalpreparedness.election.VoterInfoFragment"
        private const val electionsFragmentPackage =
            "com.example.android.politicalpreparedness.election.ElectionsFragment"
        private const val representativePackage =
            "com.example.android.politicalpreparedness.representative.RepresentativeFragment"

        @BeforeClass
        @JvmStatic
        fun setDevicePreferences() {
            //set up animations settings = off
            uiDevice = UiDevice.getInstance(getInstrumentation())
            uiDevice.executeShellCommand(ANIMATION_OFF)
            uiDevice.executeShellCommand(TRANS_ANIMATION_OFF)
            uiDevice.executeShellCommand(WIN_ANIMATION_OFF)
        }
    }


    private lateinit var repository: RepositoryInterface

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    private val context = ApplicationProvider.getApplicationContext<Context>()

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
        repository = ServiceLocator.provideRepository(ApplicationProvider.getApplicationContext())

        runBlocking {
            repository.deleteAllElections()
            repository.deleteAllSavedElections()
        }
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

        navToRepresentativeScreenButton.click()

        representativeTitle.isDisplayed()

        scenario.close()
    }

    @Test
    fun followAnElection() {

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        //navigate to the Elections screen
        navToUpComingElectionsButton.click()

        // navigate to Item(election) at index 0 Details screen
        electionsRecyclerView
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                    0,
                    click()
                )
            )

        uiDevice.waitForWindowUpdate(voterInfoFragmentPackage, ONE_SEC)

        // verify - we are on the correct election instance screen
        onView(withText("VIP Test Election")).isDisplayed()

        // verify - follow button is displayed, and click it
        followOrUnFollowButton.isDisplayed()
        followOrUnFollowButton.click()

        //return to elections screen
        pressBack()
        electionsRecyclerView.isDisplayed()

        // open/show Saved Elections
        uiDevice.pressMenu()
        savedElectionsMenuItem.click()

        // THEN - verify the election is saved/followed
        onView(withText("VIP Test Election")).isDisplayed()

        scenario.close()

    }

    @Test
    fun unfollowAnElection() = runTest {

        //GIVEN -  activity is launched, and...
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        navToUpComingElectionsButton.click()

        //an election is followed
        onView(withText("VIP Test Election")).click()
        onView(withText(context.getString(R.string.follow_btn_txt))).click()

        pressBack()
        uiDevice.pressMenu()
        savedElectionsMenuItem.click()

        //WHEN - this election is unfollowed
        onView(withText("VIP Test Election")).click()
        onView(withText(context.getString(R.string.unfollow_btn_txt))).click()
        pressBack()

        // THEN - no election appears in the saved list
        onView(withText(context.getString(R.string.no_data_available_msg))).isDisplayed()

        scenario.close()
    }

    @Test
    fun representativeScreen_useMyLocation() = runTest {

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        // navigate to representative screen
        navToRepresentativeScreenButton.click()

        // WHEN - useMyLocation button is clicked
        useMyLocationButton.click()

        // wait for data to appear
        uiDevice.wait(Until.gone(By.text(context.getString(R.string.no_data_available_msg))), 1000)

        // THEN - verify 'President of the United States' appears
        onView(withText("President of the United States")).isDisplayed()

        scenario.close()
    }

    @Test
    fun representativeScreen_useMyLocation_dragListUp() = runTest {

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        //navigate to the representative screen
        navToRepresentativeScreenButton.click()

        // WHEN - useMyLocation button is clicked
        useMyLocationButton.click()

        // wait for data to appear
        uiDevice.wait(Until.gone(By.text(context.getString(R.string.no_data_available_msg))), 1000)

        //verify 'President of the United States' appears
        onView(withText("President of the United States")).isDisplayed()

        // THEN -  we are able to drag the list up.
        val o =
            uiDevice.findObject(UiSelector().text(context.getString(R.string.representative_search_text)))
        uiDevice.findObject(UiSelector().description(context.getString(R.string.cd_representative_recyclerview)))
            .dragTo(o, 2)

        scenario.close()

    }


    @Test
    fun representativeScreen_useMyLocation_visitWebsite() {

        // initialise intent for intent validation
        Intents.init()

        intending(not(IntentMatchers.isInternal())).respondWith(
            Instrumentation.ActivityResult(
                0,
                null
            )
        )

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        //navigate to the representative screen
        navToRepresentativeScreenButton.click()

        useMyLocationButton.click()

        // wait for data to appear
        uiDevice.wait(Until.gone(By.text(context.getString(R.string.no_data_available_msg))), 1000)

        // WHEN - web link is clicked for list item with text President of the United States)
        onView(
            allOf(
                withId(R.id.web_img),
                hasSibling(withText("President of the United States"))
            )
        ).perform(click())

        // THEN - verify the correct url is captured in the intent.
        intended(IntentMatchers.hasData(Uri.parse("https://www.whitehouse.gov/")))

        Intents.release()

        scenario.close()
    }

    @Test
    fun representativeScreen_useMyLocation_pos20_visitTwitter() {

        // initialise the intent for intent validation
        Intents.init()

        intending(not(IntentMatchers.isInternal())).respondWith(
            Instrumentation.ActivityResult(
                0,
                null
            )
        )

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        navToRepresentativeScreenButton.click()

        useMyLocationButton.click()

        // wait for data to appear
        uiDevice.wait(Until.gone(By.text(context.getString(R.string.no_data_available_msg))), 1000)

        // drag the list up
        val o =
            uiDevice.findObject(UiSelector().text(context.getString(R.string.representative_search_text)))
        uiDevice.findObject(UiSelector().description(context.getString(R.string.cd_representative_recyclerview)))
            .dragTo(o, 2)

        // scroll to position 20 in the recycler view
        representativesRecyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                20, scrollTo()
            )
        )


        // WHEN - the twitter icon of item at position 20 is clicked
        onView(
            allOf(
                withId(R.id.twitter_img),
                hasSibling(withText("Jeffrey F. Rosen"))
            )
        ).click()

        // THEN - validate that the intent captured has url host twitter.com
        intended(IntentMatchers.hasData(UriMatchers.hasHost("www.twitter.com")))

        Intents.release()

        scenario.close()
    }

    @Test
    @Ignore("will implement later as it is similar to the above two tests")
    fun representativeScreen_useMyLocation_visitFacebook() {
    }

    @Test
    fun representativeScreen_noAddressProvided_findMyRepresentatives_showsSnackBarError() {

        // GIVEN - activity is launched
        val scenario = launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        navToRepresentativeScreenButton.click()

        // WHEN - no address provided and findMyRepresentative button is clicked
        findMyRepresentativesButton.click()

        val errorText =
            context.applicationContext.getString(R.string.no_address_provided_error_text)

        // THEN - verify that the Snack-bar is shown with the correct error text
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(errorText)))

    }
}