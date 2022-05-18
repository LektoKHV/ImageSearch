package ru.vldkrt.imagesearch.business.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.paging.map
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.business.master.ListViewModel
import ru.vldkrt.imagesearch.databinding.FragmentImagePagerBinding
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.entities.asUI

@AndroidEntryPoint
class ImagePagerFragment : Fragment() {

    private val args: ImagePagerFragmentArgs by navArgs()
    private val listViewModel: ListViewModel by navGraphViewModels(R.id.listFragment)

    private var _binding: FragmentImagePagerBinding? = null
    private val binding get() = _binding!!

    private var _adapter: FragmentPagerAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImagePagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = FragmentPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listViewModel.images.collect {

//                    it.
//                        val images = it.data.imagesResults
//                        adapter.updateList(images)
//                        val currentItemIndex = images.map { it.asUI() }.indexOf(args.image)
//                        binding.viewPager.setCurrentItem(currentItemIndex, false)
//                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        _adapter = null
    }
}