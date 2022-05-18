package ru.vldkrt.imagesearch.business.detail

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.databinding.FragmentImageBinding
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.view.OnItemClickListener

class PagerAdapter(
    var onItemClickListener: OnItemClickListener<ImageResult>? = null,
) : RecyclerView.Adapter<PagerAdapter.ImageViewHolder>() {

    private val list: MutableList<ImageResult> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PagerAdapter.ImageViewHolder {
        return ImageViewHolder(
            FragmentImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PagerAdapter.ImageViewHolder, position: Int) {
        val item = list[position]

        holder.bind(item)
    }

    override fun getItemCount() = list.size

    inner class ImageViewHolder(private val binding: FragmentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageResult) {
            
        }
    }
}