package com.example.android.politicalpreparedness

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election


@BindingAdapter("electionListData")
fun bindElectionRecyclerView(recyclerView: RecyclerView, electionsData:List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    Log.e("BINDING ADAPTER: ",electionsData?.size.toString())
    adapter.submitList(electionsData)
}