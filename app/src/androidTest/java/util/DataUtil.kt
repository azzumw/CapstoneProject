package util

import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

fun createSomeElections(): List<Election> {
    val localDate = Date(1220227200L * 1000)

    return List(3) {
        Election(
            it, "Election $it", localDate,
            Division("$it-division", "USA", "California")
        )
    }
}