package com.danrsy.rstoryapp.ui.story

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.data.model.story.StoryResponse
import com.danrsy.rstoryapp.databinding.FragmentStoryBinding
import com.danrsy.rstoryapp.ui.adapter.StoryAdapter
import com.danrsy.rstoryapp.ui.story.add.AddStoryActivity
import com.danrsy.rstoryapp.utils.Resource


class StoryFragment : Fragment() {

    private var authToken: String? = null
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding
    private val viewModel: StoryViewModel by viewModels()
    private lateinit var loginPreference: UserPreference
    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginPreference = UserPreference(requireActivity())
        authToken = loginPreference.getUser().token

        binding?.fabAddStory?.setOnClickListener {
            startActivity(Intent(requireActivity(), AddStoryActivity::class.java))
        }

        authToken?.let { getData(it) }

    }

    override fun onResume() {
        super.onResume()
        authToken?.let { getData(it) }
    }

    private fun getData(authToken: String) {
        viewModel.getListStory("Bearer $authToken").observe(requireActivity()) {
            when (it) {
                is Resource.Loading -> {
                    showLoadingState(true)
                    showErrorMsg(false, "")
                    showEmptyState(false)
                }

                is Resource.Success -> {
                    showLoadingState(false)
                    showErrorMsg(false, "")
                    val data = it.data?.listStory
                    if (data.isNullOrEmpty()) {
                        showEmptyState(true)
                    } else {
                        populateData(data)
                        showEmptyState(false)
                    }
                }

                is Resource.Error -> {
                    showLoadingState(false)
                    showErrorMsg(true, it.message)
                    showEmptyState(false)
                }
            }
        }
    }

    private fun populateData(data: List<StoryResponse>) {
        adapter = StoryAdapter(data)
        binding?.apply {
            rvStory.layoutManager = LinearLayoutManager(requireContext())
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(state: Boolean) {
        binding?.apply {
            emptyState.root.visibility = if (state) View.VISIBLE else View.GONE
            rvStory.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun showErrorMsg(state: Boolean, msg: String?) {
        binding?.apply {
            rvStory.visibility = if (state) View.GONE else View.VISIBLE
            errorState.root.visibility = if (state) View.VISIBLE else View.GONE
            emptyState.massage.text = msg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}