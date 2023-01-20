import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.android.politicalpreparedness.MainActivity
import com.example.android.politicalpreparedness.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTests {



    @Before
    fun setUp() {
    }

    @Test
    fun launchActivity() {
       launch(MainActivity::class.java)

//        onView(withId(R.id.upComingElectionsButton)).check(matches(isDisplayed()))
    }
}