package ru.vldkrt.imagesearch.business.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import ru.vldkrt.imagesearch.view.OnItemClickListener
import ru.vldkrt.imagesearch.databinding.ItemImageBinding
import ru.vldkrt.imagesearch.domain.entities.ImageResult

class ImageAdapter(
    var onItemClickListener: OnItemClickListener<ImageResult>? = null,
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val list: MutableList<ImageResult> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item: ImageResult = list[position]
        holder.bind(item)
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<ImageResult>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
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