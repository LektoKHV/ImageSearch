package ru.vldkrt.imagesearch.business.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.paging.ItemSnapshotList
import androidx.paging.map
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.business.MainActivity
import ru.vldkrt.imagesearch.business.master.ListViewModel
import ru.vldkrt.imagesearch.business.master.PagedDataAdapter
import ru.vldkrt.imagesearch.databinding.FragmentImagePagerBinding
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.entities.asUI
import ru.vldkrt.imagesearch.util.runSafely
import ru.vldkrt.imagesearch.view.GridSpacingItemDecoration

@AndroidEntryPoint
class ImagePagerFragment : Fragment() {

    private val args: ImagePagerFragmentArgs by navArgs()
    private val listViewModel: ListViewModel by navGraphViewModels(R.id.listFragment)
    private val imagePagerViewModel: ImagePagerViewModel by navGraphViewModels(R.id.imagePagerFragment)

    private var _binding: FragmentImagePagerBinding? = null
    private val binding get() = _binding!!

    private var _adapter: PagerAdapter? = null
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
        _adapter = PagerAdapter(
            { imageView ->
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        // Scale imageView to prevent filling the space with error
                        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        // Scale up until the shortest side is equal to container
                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        startPostponedEnterTransition()
                        return false
                    }
                }
            }
        ) { v, item ->
            runSafely {
                findNavController().navigate(ImagePagerFragmentDirections.actionImagePagerFragmentToWebImageFragment(
                    item.asUI()))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Wait for selected picture to render
        postponeEnterTransition()

        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val item: ImageResult? = adapter.getItemAt(position)
                // Change title depends on current item
                val title = item?.title ?: "Image"
                (activity as? AppCompatActivity)?.supportActionBar?.title = title

                if (item != null) imagePagerViewModel.currentItem = item
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Coroutine scrolls list to current element
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.onPagesUpdatedFlow.collect {
                        val images: ItemSnapshotList<ImageResult> = adapter.snapshot()
                        val currentItemPosition = images.indexOf(imagePagerViewModel.currentItem)
                        binding.viewPager.setCurrentItem(currentItemPosition, false)
                    }
                }

                // Updating data from image list scoped in ListFragment
                viewLifecycleOwner.lifecycleScope.launch {
                    listViewModel.images.collect {
                        adapter.submitData(viewLifecycleOwner.lifecycle, it)
                    }
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