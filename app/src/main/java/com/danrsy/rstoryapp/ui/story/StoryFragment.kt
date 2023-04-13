package com.danrsy.rstoryapp.ui.story

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.danrsy.rstoryapp.data.local.auth.UserPreference
import com.danrsy.rstoryapp.databinding.FragmentStoryBinding
import com.danrsy.rstoryapp.ui.story.add.AddStoryActivity
import com.danrsy.rstoryapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding
    private val viewModel: StoryViewModel by viewModels()
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

        loginPreference = UserPreference(requireContext())
        val authToken = loginPreference.getUser().token

        binding?.fabAddStory?.setOnClickListener {
            startActivity(Intent(requireActivity(), AddStoryActivity::class.java))
        }

        CoroutineScope(Dispatchers.Main).launch {
            if (authToken != null) {
                viewModel.getListStory(authToken).observe(requireActivity()) {
                    when (it) {
                        is Resource.Loading -> {
                            showLoadingState(true)
                            showErrorMsg(false, "")
                        }
                        is Resource.Success -> {
                            showLoadingState(false)
                            showErrorMsg(false, "")
                            val data = it.data?.listStory
                            if (data.isNullOrEmpty()) {
                                showEmptyState(true)
                            } else {
                                //
                            }
                        }
                        is Resource.Error -> {
                            showLoadingState(false)
                            showErrorMsg(true, it.message)
                        }
                    }
                }
            }
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