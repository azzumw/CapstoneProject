package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModels{
        ElectionsViewModelFactory(ElectionDatabase.getInstance(requireContext()).electionDao)
    }

    private var _binding: FragmentElectionBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)

        binding.electionsViewModel = electionsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.upComingElectionsRecyclerView.adapter =
            ElectionListAdapter(ElectionListAdapter.ElectionListener {
                electionsViewModel.displayElectionVoterInfo(it)
            })

        electionsViewModel.navToSingleElectionVoterInfo.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id,
                        it.division
                    )
                )
                electionsViewModel.onNavComplete()
            }
        })

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}