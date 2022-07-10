package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.data.RemoteDataSource
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.TheRepository
import java.util.*

class ElectionsFragment : Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModels {
        ElectionsViewModelFactory(TheRepository(
            LocalDataSource(ElectionDatabase.getInstance(requireContext()).electionDao), RemoteDataSource
        ))
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

        electionsViewModel.status.observe(viewLifecycleOwner, Observer {
            if (it == ElectionsApiStatus.ERROR) {
                binding.errorStatusMsg.visibility = View.VISIBLE
            }
        })


        electionsViewModel.filteredElections.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.noDataInDbMsg.visibility = View.VISIBLE
            } else {
                binding.noDataInDbMsg.visibility = View.GONE
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all_elections -> {
                electionsViewModel.selectFilter(1)
            }

            R.id.saved_elections -> {
                electionsViewModel.selectFilter(2)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}