package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.google.android.material.snackbar.Snackbar

private const val ALL_ELECTIONS  = 1
private const val SAVED_ELECTIONS  = 2
class ElectionsFragment : Fragment() {

    private val electionsViewModel: ElectionsViewModel by viewModels {
        ElectionsViewModelFactory(
            (requireActivity().application as MyApplication).repository
        )
    }

    private var _binding: FragmentElectionBinding? = null
    private val binding get() = _binding!!

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

        electionsViewModel.filteredElections.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {

                binding.noDataInDbMsg.visibility = View.VISIBLE
            } else {
                binding.noDataInDbMsg.visibility = View.GONE

            }
        })

        electionsViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "You are offline.", Snackbar.LENGTH_SHORT).show()
                electionsViewModel.doneShowingSnackBar()
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

                electionsViewModel.selectFilter(ALL_ELECTIONS)
            }

            R.id.saved_elections -> {
                electionsViewModel.selectFilter(SAVED_ELECTIONS)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

