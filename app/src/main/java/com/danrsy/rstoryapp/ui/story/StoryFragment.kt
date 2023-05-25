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
import com.danrsy.rstoryapp.databinding.FragmentStoryBinding
import com.danrsy.rstoryapp.ui.adapter.LoadingStateAdapter
import com.danrsy.rstoryapp.ui.adapter.StoryListAdapter
import com.danrsy.rstoryapp.ui.story.add.AddStoryActivity
import com.danrsy.rstoryapp.utils.ViewModelFactory


class StoryFragment : Fragment() {

    private var authToken: String? = null
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding
    private val viewModel: StoryViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }
    private lateinit var loginPreference: UserPreference

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
        binding?.apply {
            rvStory.layoutManager = LinearLayoutManager(requireActivity())
        }

        authToken?.let {
            getData(it)
        }

    }

    override fun onResume() {
        super.onResume()
        authToken?.let { getData(it) }
    }

    private fun getData(authToken: String) {

        val adapter = StoryListAdapter()
        binding?.rvStory?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getStory("Bearer $authToken").observe(requireActivity()) {
            adapter.submitData(lifecycle, it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}