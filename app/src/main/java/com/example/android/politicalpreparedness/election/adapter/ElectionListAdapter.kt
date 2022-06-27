package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ListItemViewBinding
//import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionAndSavedElection
//import com.example.android.politicalpreparedness.network.models.TheElection

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<ElectionAndSavedElection, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
    //to ensure card view width is match parent
        return ElectionViewHolder(ListItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener.onClick(election)
        }
        holder.bind(election)
    }


    class ElectionViewHolder(val binding:ListItemViewBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(election: ElectionAndSavedElection){
            binding.electionAndSaved = election
            binding.executePendingBindings()
        }
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<ElectionAndSavedElection>(){
        //This method is called by DiffUtil to decide whether two objects represent the same Item.
        //DiffUtil uses this method to figure out if the new Election object is the same as the old Election object.
        override fun areItemsTheSame(oldItem: ElectionAndSavedElection, newItem: ElectionAndSavedElection): Boolean {
            return oldItem == newItem
        }

        //This method is called by DiffUtil when it wants to check whether two items have the same data
        override fun areContentsTheSame(oldItem: ElectionAndSavedElection, newItem: ElectionAndSavedElection): Boolean {
            return oldItem.election.id == newItem.election.id
        }

    }

    class ElectionListener(val clickListener:(election:ElectionAndSavedElection) -> Unit){
        fun onClick(election: ElectionAndSavedElection) = clickListener(election)
    }
}




