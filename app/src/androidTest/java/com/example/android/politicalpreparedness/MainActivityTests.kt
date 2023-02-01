package com.example.android.politicalpreparedness

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Before
import org.junit.Test

class MainActivityTests {


    @Before
    fun setUp() {
    }

    @Test
    fun launchActivity() {
        launch(MainActivity::class.java)

        onView(withId(R.id.upComingElectionsButton))
            .perform(click())

    }
}