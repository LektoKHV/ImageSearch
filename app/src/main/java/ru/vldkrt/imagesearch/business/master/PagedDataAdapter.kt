package ru.vldkrt.imagesearch.business.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import ru.vldkrt.imagesearch.databinding.ItemImageBinding
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.view.ImageResultComparator
import ru.vldkrt.imagesearch.view.OnItemClickListener

class PagedDataAdapter(
    var onItemClickListener: OnItemClickListener<ImageResult>? = null,
) : PagingDataAdapter<ImageResult, PagedDataAdapter.ImageViewHolder>(
    diffCallback = ImageResultComparator()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder = ImageViewHolder(
        ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item: ImageResult = getItem(position) ?: return
        holder.bind(item)
    }

    // 'inner' means object has a reference to outer class instance, we can use its functions or
    // fields, like OnItemClickListener. Also it means ImageViewHolder cannot live without enclosing
    // ImageAdapter.
    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageResult) {
            with(binding) {
                // Set unique transition name for future navigation
                imageView.transitionName = item.original

                val circularProgressDrawable = CircularProgressDrawable(root.context).apply {
                    strokeWidth = 4f
                    centerRadius = 40f
                    start()
                }
                Glide.with(root.context)
                    .load(item.thumbnail)
                    .placeholder(circularProgressDrawable)
                    .into(imageView)

                root.setOnClickListener {
                    onItemClickListener?.onItemClick(it, item)
                }
            }
        }
    }
}