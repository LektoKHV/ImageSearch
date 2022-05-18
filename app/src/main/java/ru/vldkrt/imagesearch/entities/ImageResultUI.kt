package ru.vldkrt.imagesearch.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vldkrt.imagesearch.domain.entities.ImageResult

@Parcelize
data class ImageResultUI(
    val isProduct: String? = null,

    val thumbnail: String,

    val original: String,

    val link: String,

    val position: Int,

    val source: String,

    val title: String,

    val inStock: String? = null
) : Parcelable

fun ImageResult.asUI() =
    ImageResultUI(isProduct, thumbnail, original, link, position, source, title, inStock)

fun ImageResultUI.asDomain() =
    ImageResult(isProduct, thumbnail, original, link, position, source, title, inStock)
