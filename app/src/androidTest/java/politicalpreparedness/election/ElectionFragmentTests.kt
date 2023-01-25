package politicalpreparedness.election


import android.os.SystemClock
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import repository.FakeRepository

/*
* Important! Cannot execute fragment tests until this issue is resolved:
* https://issuetracker.google.com/issues/128612536#comment22
* */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ElectionFragmentTests {

    private lateinit var repository : RepositoryInterface

//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        //https://stackoverflow.com/questions/72218645/shared-srcdirs-between-test-and-androidtest-unresolved-references-after-upgrade?noredirect=1&lq=1
        repository = FakeRepository()
        ServiceLocator.repository = repository
    }


    @Test
    fun launchFragment() {

        launchFragmentInContainer<ElectionsFragment>(null,R.style.AppTheme)
        SystemClock.sleep(2000)

    }
}