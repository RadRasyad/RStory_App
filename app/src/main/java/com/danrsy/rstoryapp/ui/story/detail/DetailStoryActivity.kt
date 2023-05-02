package com.danrsy.rstoryapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.danrsy.rstoryapp.R
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.data.model.story.StoryResponse
import com.danrsy.rstoryapp.databinding.ActivityDetailStoryBinding
import com.danrsy.rstoryapp.utils.Resource

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var loginPreference: UserPreference
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = UserPreference(this)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra(EXTRA_DATA)
        if (id != null) {
            getData(id)
        }
    }

    private fun getData(id: String) {

        val authToken = loginPreference.getUser().token
        if (authToken != null) {
            detailViewModel.getDetailStory("Bearer $authToken", id)
                .observe(this@DetailStoryActivity) {
                    when (it) {
                        is Resource.Loading -> showLoadingState(true)
                        is Resource.Success -> {
                            showLoadingState(false)
                            val data = it.data?.listStory
                            if (data != null) {
                                populateData(data)
                            }
                        }

                        is Resource.Error -> {
                            showLoadingState(false)
                            Toast.makeText(
                                this@DetailStoryActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
        }

    }

    private fun populateData(data: StoryResponse) {

        binding.apply {
            tvName.text = data.name
            tvDescription.text = data.description
            ivPhoto.load(data.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_img)
                error(R.drawable.placeholder_img)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}