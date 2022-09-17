package com.te6lim.c2candroidassessment.screens.exhibitList

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.te6lim.c2candroidassessment.MainActivity
import com.te6lim.c2candroidassessment.R
import com.te6lim.c2candroidassessment.database.DatabaseExhibitLoader
import com.te6lim.c2candroidassessment.database.ExhibitDatabase
import com.te6lim.c2candroidassessment.databinding.FragmentListExhibitBinding
import com.te6lim.c2candroidassessment.network.ExhibitApi
import com.te6lim.c2candroidassessment.network.RestExhibitLoader
import com.te6lim.c2candroidassessment.repository.ExhibitRepository
import com.te6lim.c2candroidassessment.repository.LoadSource
import com.te6lim.c2candroidassessment.repository.LoadState
import com.te6lim.c2candroidassessment.repository.LoadStateListener

class ExhibitListFragment : Fragment() {

    private lateinit var binding: FragmentListExhibitBinding

    private lateinit var networkStateScreens: List<View>

    private lateinit var loadIndicatorAnimator: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_exhibit, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.exhibitToolbar)

        binding.lifecycleOwner = this

        loadIndicatorAnimator = getLoadIndicatorAnimator()

        val loadStateListener = object : LoadStateListener {

            override fun onStateResolved(state: LoadState, source: LoadSource) {
                showScreenBasedOnNetworkState(state, networkStateScreens)

                val pair = getResourcePairForLoadIndicator(source)

                startAnimationByState(state, pair.first, pair.second)
            }

            override fun onRefresh(isSuccess: Boolean) {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        val exhibitDatabase = ExhibitDatabase.getInstance(requireContext())

        val networkLoader = RestExhibitLoader(ExhibitApi, loadStateListener)
        val databaseLoader = DatabaseExhibitLoader(exhibitDatabase, loadStateListener)

        val viewModelProvider = ExhibitListViewModel.Factory(ExhibitRepository(networkLoader, databaseLoader))

        val viewModel = ViewModelProvider(this, viewModelProvider)[ExhibitListViewModel::class.java].apply {
            binding.swipeRefresh.isRefreshing = true
        }

        val adapter = ExhibitListAdapter()
        binding.recyclerView.adapter = adapter

        networkStateScreens = getNetworkScreenList(binding)

        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.exhibit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.refresh -> viewModel.refreshList()
                }
                return true
            }
        }

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.retry.setOnClickListener {
            viewModel.refreshList()
        }

        viewModel.exhibitList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshList()
        }

        return binding.root
    }

    private fun startAnimationByState(
        state: LoadState,
        resource: Drawable?,
        string: String
    ) {
        when (state) {
            LoadState.LOADING -> {

                binding.loadStateImage.setImageDrawable(resource)
                binding.loadText.text = string
                with(loadIndicatorAnimator) {
                    end()
                    start()
                }
            }
            else -> {}
        }
    }

    private fun getResourcePairForLoadIndicator(source: LoadSource): Pair<Drawable?, String> {
        val image: Drawable
        val text: String
        when (source) {
            LoadSource.NETWORK -> {
                image = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_online)!!
                text = requireContext().getString(R.string.loading_from_network)
            }

            LoadSource.DATABASE -> {
                image = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_offline)!!
                text = requireContext().getString(R.string.loading_from_database)
            }
        }
        return Pair(image, text)
    }

    private fun getLoadIndicatorAnimator(): ObjectAnimator {
        return ObjectAnimator.ofFloat(
            binding.loadingIndicator, View.ALPHA, 1f
        ).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            duration = 2000
        }
    }

    private fun getNetworkScreenList(binding: FragmentListExhibitBinding): List<View> {
        return listOf(binding.loadingScreen, binding.retryScreen, binding.recyclerView)
    }

    private fun showScreenBasedOnNetworkState(state: LoadState, views: List<View>) {
        when (state) {
            LoadState.LOADING -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.loadingScreen }?.visibility = View.VISIBLE
            }

            LoadState.DONE -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.recyclerView }?.visibility = View.VISIBLE
            }

            LoadState.ERROR -> {
                views.forEach { it.visibility = View.GONE }
                views.find { it == binding.retryScreen }?.visibility = View.VISIBLE
            }
        }
    }
}