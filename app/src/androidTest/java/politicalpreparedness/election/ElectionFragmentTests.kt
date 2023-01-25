package politicalpreparedness.election


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.repository.RepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
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
    fun launchFragment() = runBlockingTest{


    }
}