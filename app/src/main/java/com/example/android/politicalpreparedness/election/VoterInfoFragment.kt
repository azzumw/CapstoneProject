package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.repository.TheRepository

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


        return binding.root
    }

    //TODO: Create method to load URL intents

}