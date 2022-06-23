package com.example.android.politicalpreparedness

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.ElectionsApiStatus
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election


@BindingAdapter("electionListData")
fun bindElectionRecyclerView(recyclerView: RecyclerView, electionsData:List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    Log.e("BINDING ADAPTER: ",electionsData?.size.toString())
    adapter.submitList(electionsData)
}

@BindingAdapter("electionsApiStatus")
fun electionsApiStatus(imgView: ImageView,status:ElectionsApiStatus){
    when(status){
        ElectionsApiStatus.LOADING -> {
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.loading_animation)
        }
        ElectionsApiStatus.DONE -> {
            imgView.visibility = View.GONE
        }
        ElectionsApiStatus.ERROR ->{
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}