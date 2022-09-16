package com.te6lim.c2candroidassessment.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.te6lim.c2candroidassessment.MainActivity
import com.te6lim.c2candroidassessment.R
import com.te6lim.c2candroidassessment.database.ExhibitDatabase
import com.te6lim.c2candroidassessment.databinding.FragmentListExhibitBinding
import com.te6lim.c2candroidassessment.model.RestExhibitLoader
import com.te6lim.c2candroidassessment.network.ExhibitApi
import com.te6lim.c2candroidassessment.network.ExhibitApiService
import com.te6lim.c2candroidassessment.repository.ExhibitRepository

class ExhibitListFragment : Fragment() {

    private lateinit var binding: FragmentListExhibitBinding

    private lateinit var networkStateScreens: List<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_exhibit, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.exhibitToolbar)

        binding.lifecycleOwner = this

        val adapter = ExhibitListAdapter()
        binding.recyclerView.adapter = adapter

        networkStateScreens = getNetworkScreenList(binding)

        val exhibitDatabase = ExhibitDatabase.getInstance(requireContext())
        val exhibitLoader = RestExhibitLoader(ExhibitApi, object : RestExhibitLoader.NetworkStateListener {
            override fun onStateResolved(state: RestExhibitLoader.NetworkState) {
                showScreenBasedOnNetworkState(state, networkStateScreens)
            }
        })

        val viewModelProvider = ExhibitListViewModel.Factory(
            ExhibitRepository(exhibitLoader, exhibitDatabase)
        )

        val viewModel = ViewModelProvider(this, viewModelProvider)[ExhibitListViewModel::class.java]

        binding.retry.setOnClickListener {
            viewModel.refreshList()
        }

        viewModel.exhibitList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    private fun getNetworkScreenList(binding: FragmentListExhibitBinding): List<View> {
        return listOf(binding.loadingScreen, binding.retryScreen, binding.recyclerView)
    }

    private fun showScreenBasedOnNetworkState(state: RestExhibitLoader.NetworkState, views: List<View>) {
        when (state) {
            RestExhibitLoader.NetworkState.LOADING -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.loadingScreen }?.visibility = View.VISIBLE
            }

            RestExhibitLoader.NetworkState.DONE -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.recyclerView }?.visibility = View.VISIBLE
            }

            RestExhibitLoader.NetworkState.ERROR -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.retryScreen }?.visibility = View.VISIBLE
            }
        }
    }
}