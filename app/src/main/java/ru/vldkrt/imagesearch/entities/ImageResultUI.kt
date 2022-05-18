package ru.vldkrt.imagesearch.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.vldkrt.imagesearch.domain.entities.ImageResult

@Parcelize
data class ImageResultUI(

    val thumbnail: String,

    val original: String,

    val link: String,

    val position: Int,

    val source: String,

    val title: String,
) : Parcelable

fun ImageResult.asUI() =
    ImageResultUI(thumbnail, original, link, position, source, title)
