package ru.vldkrt.imagesearch.business.master

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.databinding.FragmentListBinding

import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import ru.vldkrt.imagesearch.view.GridSpacingItemDecoration
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.entities.asUI
import ru.vldkrt.imagesearch.util.runSafely


@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: ListViewModel by navGraphViewModels(R.id.listFragment) {
        defaultViewModelProviderFactory
    }

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var _adapter: PagedDataAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        _adapter = PagedDataAdapter { v, item ->
            val extras = FragmentNavigatorExtras(v to item.original)
            runSafely {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToImagePagerFragment(
                    item.asUI()), extras)
            }
        }

        // This block needs for returning from detail fragment to animate bounds change backward
        postponeEnterTransition()
        binding.imagesView.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LifeCycle", "onViewCreated()")

        binding.imagesView.adapter = adapter.withLoadStateFooter(
            footer = DiscoverPagedLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState: CombinedLoadStates ->
            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            binding.emptyView.isVisible = isListEmpty
            binding.imagesView.isVisible = !isListEmpty

            // Show the list only if refresh succeeds.
            binding.imagesView.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show retry button if load failed
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
        }
        binding.imagesView.addItemDecoration(GridSpacingItemDecoration(2, R.dimen.item_space, 0))
        binding.retryButton.setOnClickListener { adapter.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.images.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
                val snapshot = adapter.snapshot()
                "dsds"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("LifeCycle", "onCreate()")
    }

    override fun onStart() {
        super.onStart()

        Log.d("LifeCycle", "onStart()")
    }

    override fun onResume() {
        super.onResume()

        Log.d("LifeCycle", "onResume()")
    }

    override fun onPause() {
        super.onPause()

        Log.d("LifeCycle", "onPause()")
    }

    override fun onStop() {
        super.onStop()

        Log.d("LifeCycle", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("LifeCycle", "onDestroy()")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search_hint)
        searchView.setQuery(viewModel.query, false)
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                println("onQueryTextSubmit")

                viewModel.search()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("onQueryTextChange")
                viewModel.setQuery(newText)
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifeCycle", "onDestroyView()")
        _binding = null
        _adapter = null
    }
}