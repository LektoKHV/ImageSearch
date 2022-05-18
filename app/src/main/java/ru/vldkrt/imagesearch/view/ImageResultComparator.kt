package ru.vldkrt.imagesearch.view

import androidx.recyclerview.widget.DiffUtil
import ru.vldkrt.imagesearch.domain.entities.ImageResult

class ImageResultComparator : DiffUtil.ItemCallback<ImageResult>() {
    override fun areItemsTheSame(
        oldItem: ImageResult,
        newItem: ImageResult
    ): Boolean =
        oldItem.original == newItem.original

    override fun areContentsTheSame(
        oldItem: ImageResult,
        newItem: ImageResult
    ): Boolean =
        oldItem == newItem
}