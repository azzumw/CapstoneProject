package com.example.android.politicalpreparedness.representative.adapter

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.ApiStatus
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.android.synthetic.*

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {

    view.setImageResource(R.drawable.ic_profile)

    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()

        Glide
            .with(view)
            .load(uri)
            .transition(withCrossFade())
            .circleCrop()
            .apply(RequestOptions().placeholder(R.drawable.ic_profile))
            .into(view)
    }
}

@BindingAdapter("representativeListData")
fun bindRepresentativeRecyclerView(
    recyclerView: RecyclerView,
    representativeData: List<Representative>?
) {
    val adapter = recyclerView.adapter as RepresentativeListAdapter
    Log.e("REP BIND ADAPTER: ", representativeData?.size.toString())
    adapter.submitList(representativeData)
//    if(representativeData.isNullOrEmpty()){
//        recyclerView.visibility = View.GONE
//    }else{
//        recyclerView.visibility = View.VISIBLE
//    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

@BindingAdapter("apiStatus")
fun apiStatus(imgView: ImageView, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.DONE -> {
            imgView.visibility = View.GONE
        }
        ApiStatus.ERROR -> {
            imgView.visibility = View.VISIBLE
            imgView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}

@BindingAdapter("listPlaceHolder")
fun bindRepresentativePlaceholder(
    textView: TextView,
    representativeData: List<Representative>?
) {
    textView.visibility = View.GONE

    if(representativeData.isNullOrEmpty()) {
        textView.visibility = View.VISIBLE
    } else {
//        textView.text = ""
        textView.visibility =  View.GONE
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}
