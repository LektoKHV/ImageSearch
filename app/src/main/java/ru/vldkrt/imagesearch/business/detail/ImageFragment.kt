package ru.vldkrt.imagesearch.business.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.databinding.FragmentImageBinding
import ru.vldkrt.imagesearch.util.runSafely

@AndroidEntryPoint
class ImageFragment : Fragment() {

    private val args: ImageFragmentArgs by navArgs()
    private var _binding: FragmentImageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(args.image)
        val url = args.image.original

        postponeEnterTransition()

        binding.imageView.apply {
            transitionName = url
            startPostponedEnterTransitionAfterLoadingImage(url, this)
        }

        binding.sourceButton.setOnClickListener {
            runSafely { findNavController().navigate(ImagePagerFragmentDirections.actionImagePagerFragmentToWebImageFragment(args.image)) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Use this method to show transition when image is finally loaded
     * */
    private fun startPostponedEnterTransitionAfterLoadingImage(
        url: String,
        imageView: ImageView
    ) {
        Glide.with(this)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
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
                    isFirstResource: Boolean
                ): Boolean {
                    // Scale up until the shortest side is equal to container
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    startPostponedEnterTransition()
                    return false
                }
            })
            .error(R.drawable.ic_error)
            .into(imageView)
    }
}