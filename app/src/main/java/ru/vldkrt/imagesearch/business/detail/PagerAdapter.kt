package ru.vldkrt.imagesearch.business.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.databinding.ItemImageFullscreenBinding
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.view.ImageResultComparator
import ru.vldkrt.imagesearch.view.OnItemClickListener

class PagerAdapter(
    // Avoid using the same listener for every image request, since it invokes linked image view
    private val imageLoadListenerFactory: (ImageView) -> RequestListener<Drawable>,
    var onItemClickListener: OnItemClickListener<ImageResult>? = null,
) : PagingDataAdapter<ImageResult, PagerAdapter.ImageViewHolder>(
    diffCallback = ImageResultComparator()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PagerAdapter.ImageViewHolder {
        return ImageViewHolder(
            ItemImageFullscreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PagerAdapter.ImageViewHolder, position: Int) {
        val item: ImageResult = getItem(position) ?: return
        holder.bind(item)
    }

    fun getItemAt(position: Int) = getItem(position)

    inner class ImageViewHolder(private val binding: ItemImageFullscreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageResult) {
            with(binding) {
                imageView.transitionName = item.original

                // Use listener to start postponed transition from owner fragment when pic is ready
                Glide.with(binding.root.context)
                    .load(item.original)
                    .listener(imageLoadListenerFactory(imageView))
                    .error(R.drawable.ic_error)
                    .into(imageView)

                sourceButton.setOnClickListener {
                    onItemClickListener?.onItemClick(it, item)
                }
            }
        }
    }
}