package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding : FragmentVoterInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info,container,false)


        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        val arguments = VoterInfoFragmentArgs.fromBundle(arguments!!)
//        val repository = TheRepository(ElectionDatabase.getInstance(requireContext()).electionDao)
        val database = ElectionDatabase.getInstance(requireContext()).electionDao
        val viewModelFactory = VoterInfoViewModelFactory(database,arguments.argElectionId,arguments.argDivision)

        val viewModel = ViewModelProvider(this,viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO: Handle loading of URLs

//        viewModel.state.observe(viewLifecycleOwner, Observer {
//            if (!it.isNullOrEmpty()){
//                binding.stateLocations.visibility = View.VISIBLE
//                binding.stateBallot.visibility = View.VISIBLE
//
//                val voterLocationUrl = it[0].electionAdministrationBody.votingLocationFinderUrl
//                val ballotInfoUrl = it[0].electionAdministrationBody.ballotInfoUrl
//
//                if (voterLocationUrl.isNullOrEmpty()||ballotInfoUrl.isNullOrEmpty()){
//                    if(voterLocationUrl.isNullOrEmpty() && ballotInfoUrl.isNullOrEmpty()){
//                        binding.stateLocations.visibility = View.GONE
//                        binding.stateBallot.visibility = View.GONE
//                        binding.noInfoTextView.visibility = View.VISIBLE
//
//                    }else if (ballotInfoUrl.isNullOrEmpty()){
//                        binding.stateBallot.visibility = View.GONE
//                    }else{
//                        binding.stateLocations.visibility = View.GONE
//                    }
//                }
//
//
//
//                binding.stateLocations.setOnClickListener{
//                    startIntentForUrl(voterLocationUrl)
//                }
//
//                binding.stateBallot.setOnClickListener{
//                    startIntentForUrl(ballotInfoUrl)
//                }
//
//            }else{
//                binding.noInfoTextView.visibility = View.VISIBLE
//            }
//        })

//        viewModel.state.observe(viewLifecycleOwner, Observer {
//            if(!it.isNullOrEmpty()){
//                binding.stateBallot.visibility = View.VISIBLE
//                binding.stateLocations.visibility = View.VISIBLE
//
//            }else{
//                binding.noInfoTextView.visibility = View.VISIBLE
//            }
//        })


        viewModel.voterLocationUrl.observe(viewLifecycleOwner, Observer { url ->
            if(url!=null){
                Log.e(this.javaClass.canonicalName,url)
                binding.stateLocations.visibility = View.VISIBLE
                binding.stateLocations.setOnClickListener {
                    startIntentForUrl(url)
                }
            }
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, Observer { url ->
            if(url!=null){
                Log.e(this.javaClass.canonicalName,url)
                binding.stateBallot.visibility = View.VISIBLE
                binding.stateBallot.setOnClickListener {
                    startIntentForUrl(url)
                }
            }
        })

        if (viewModel.isVoterAndBallotInfoNull){
            binding.noInfoTextView.visibility =View.VISIBLE

            binding.stateLocations.visibility = View.GONE
            binding.stateBallot.visibility = View.GONE
        }

        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun startIntentForUrl(stringUrl:String?){
        val stringToUrl = Uri.parse(stringUrl)
        val intent = Intent(Intent.ACTION_VIEW,stringToUrl)
        context?.startActivity(intent)
    }



}