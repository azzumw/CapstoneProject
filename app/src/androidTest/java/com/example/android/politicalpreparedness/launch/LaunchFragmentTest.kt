package com.example.android.politicalpreparedness.launch

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
class LaunchFragmentTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun navigate_to_elections_fragment() {

        val mockedNavController = mock(NavController::class.java)

        // GIVEN - Launch fragment is launched
        launchFragmentInContainer<LaunchFragment>(null, R.style.AppTheme)
            .onFragment {
                Navigation.setViewNavController(it.view!!, mockedNavController)
            }

        // WHEN - button to navigate to Election fragment is clicked
        onView(withId(R.id.upComingElectionsButton)).perform(click())

        // THEN - verify we navigate to the elections fragment
        verify(mockedNavController).navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())

    }

    @Test
    fun navigate_to_representative_fragment() {

        val mockedNavController = mock(NavController::class.java)

        // GIVEN - Launch fragment is launched
        launchFragmentInContainer<LaunchFragment>(null, R.style.AppTheme)
            .onFragment {
                Navigation.setViewNavController(it.view!!, mockedNavController)
            }

        // WHEN - button to navigate to Representative fragment is clicked
        onView(withId(R.id.findRepresentativeButton)).perform(click())

        // THEN - verify we navigate to the Representatives fragment
        verify(mockedNavController).navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }
}