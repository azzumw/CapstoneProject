package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentVoterInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)


        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        val arguments = VoterInfoFragmentArgs.fromBundle(requireArguments())

        val repository = (requireActivity().application as MyApplication).repository
        val viewModelFactory =
            VoterInfoViewModelFactory(repository, arguments.argElectionId, arguments.argDivision)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.voterLocationUrl.observe(viewLifecycleOwner, Observer { url ->
            if (url != null) {
                binding.stateLocations.visibility = View.VISIBLE
                binding.stateLocations.setOnClickListener {
                    startIntentForUrl(url)
                }
            }
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, Observer { url ->
            if (url != null) {
                binding.stateBallot.visibility = View.VISIBLE
                binding.stateBallot.setOnClickListener {
                    startIntentForUrl(url)
                }
            }
        })

        viewModel.correspondenceAddress.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.addressGroup.visibility = View.VISIBLE
            }
        })

        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, getString(R.string.offline_error_text_msg), Snackbar.LENGTH_SHORT).show()
                viewModel.doneShowingSnackBar()
            }
        })

        return binding.root
    }

    private fun startIntentForUrl(stringUrl: String?) {
        val stringToUrl = Uri.parse(stringUrl)
        val intent = Intent(Intent.ACTION_VIEW, stringToUrl)
        context?.startActivity(intent)
    }
}