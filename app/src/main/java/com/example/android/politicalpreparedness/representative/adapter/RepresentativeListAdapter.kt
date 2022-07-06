package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.RepresentativeListItemBinding
//import com.example.android.politicalpreparedness.databinding.ViewholderRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter: ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder(RepresentativeListItemBinding.inflate(from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>(){
        override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem.official.name == newItem.official.name
        }

    }
}

class RepresentativeViewHolder(val binding : RepresentativeListItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative) {
        binding.representative = item
//        binding.representativePhoto.setImageResource(R.drawable.ic_profile)
        if(item.official.channels!=null){
            showSocialLinks(item.official.channels)
        }else{
            binding.twitterImg.visibility = View.GONE
            binding.facebookImg.visibility = View.GONE
        }
        //TODO: Show www link ** Hint: Use provided helper methods
        if(item.official.urls != null){
            showWWWLinks(item.official.urls)
        }else{
            binding.webImg.visibility = View.GONE
        }
        binding.executePendingBindings()
    }

    //TODO: Add companion object to inflate ViewHolder (from)

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookImg, facebookUrl)
        }else binding.facebookImg.visibility = View.GONE

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) { enableLink(binding.twitterImg, twitterUrl) }
        else  binding.twitterImg.visibility = View.GONE
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.webImg, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }
    private fun disableLink(view: ImageView) {
        view.visibility = View.GONE
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

//TODO: Create RepresentativeListener