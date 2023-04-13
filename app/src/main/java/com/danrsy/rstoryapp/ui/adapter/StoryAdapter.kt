package com.danrsy.rstoryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.model.story.StoryResponse
import com.danrsy.rstoryapp.databinding.ItemRowStoryBinding

class StoryAdapter(private val listStory: List<StoryResponse>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemRowStoryBinding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(itemRowStoryBinding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    inner class StoryViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(story: StoryResponse) {
                binding.apply {
                    tvName.text = story.name
                    tvDescription.text = story.description
                    ivPhoto.load(story.photoUrl) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.placeholder_img)
                        error(R.drawable.placeholder_img)
                    }
                }
            }

    }
}