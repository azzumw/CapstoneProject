package util

import android.content.Context
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.*
import java.util.*


const val TXT_ELECTION_0 = "Election 0"
const val TXT_ELECTION_1 = "Election 1"
const val TXT_ELECTION_2 = "Election 2"


const val ANIMATION_OFF = "settings put global animator_duration_scale 0.0"
const val TRANS_ANIMATION_OFF = "settings put global transition_animation_scale 0.0"
const val WIN_ANIMATION_OFF = "settings put global window_animation_scale 0.0"
fun createThreeElectionInstances(): List<Election> {
    val localDate = Date(1220227200L * 1000)

    return List(3) {
        Election(
            it, "Election $it", localDate,
            Division("$it-division", "USA", "California")
        )
    }
}

fun Context.getString(str: String): String {
    return when (str) {
        "Election 0" -> getString(R.string.test_data_election_0)
        "Election 1" -> getString(R.string.test_data_election_1)
        "Election 2" -> getString(R.string.test_data_election_2)
        else -> {
            "Any Text"
        }
    }
}


fun createStates(electionId: Int, option: Int): List<State>? =
    when (option) {
        0 -> null
        1 -> emptyList()
        else -> listOf(
            State(
                name = "Baja California",
                electionAdministrationBody = AdministrationBody(
                    name = "Admino De Tijuana",
                    electionInfoUrl = "http://www.tijuana.com/${electionId}",
                    votingLocationFinderUrl = "http://www.voting-info.com/${electionId}",
                    ballotInfoUrl = "http://www.ballotinfo.com/${electionId}",
                    correspondenceAddress = Address(
                        "line1",
                        "line2",
                        "Tijuana",
                        "Baja California",
                        "10022"
                    )
                )
            ),
            State(
                name = "California",
                electionAdministrationBody = AdministrationBody(
                    name = "AdminBodyName",
                    electionInfoUrl = "http://www.${electionId}.com",
                    votingLocationFinderUrl = "http://www.voting-info.com/${electionId}",
                    ballotInfoUrl = "http://www.ballotinfo.com/$electionId}",
                    correspondenceAddress = Address(
                        "line1",
                        "line2",
                        "San Jose",
                        "California",
                        "10098"
                    )
                )
            ),
            State(
                name = "Juarez",
                electionAdministrationBody = AdministrationBody(
                    name = "AdminBodyName",
                    electionInfoUrl = "http://www.Juarez.com/${electionId}",
                    votingLocationFinderUrl = "http://www.voting-info.com/${electionId}/Juarez",
                    ballotInfoUrl = "http://www.ballotinfo.com/${electionId}/Juarez",
                    correspondenceAddress = Address(
                        "line1",
                        "line2",
                        "Juarez",
                        "Juarez",
                        "10045"
                    )
                )
            )
        )
    }




