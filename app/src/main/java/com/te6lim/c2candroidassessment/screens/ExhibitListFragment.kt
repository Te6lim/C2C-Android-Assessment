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
import com.te6lim.c2candroidassessment.databinding.FragmentListExhibitBinding
import com.te6lim.c2candroidassessment.model.RestExhibitLoader
import com.te6lim.c2candroidassessment.network.ExhibitApi
import com.te6lim.c2candroidassessment.network.ExhibitApiService

class ExhibitListFragment : Fragment() {

    private lateinit var binding: FragmentListExhibitBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_exhibit, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.exhibitToolbar)

        binding.lifecycleOwner = this

        val adapter = ExhibitListAdapter()
        binding.recyclerView.adapter = adapter

        val viewModelProvider = ExhibitListViewModel.Factory(RestExhibitLoader(ExhibitApi))
        val viewModel = ViewModelProvider(this, viewModelProvider)[ExhibitListViewModel::class.java]

        viewModel.exhibitList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}