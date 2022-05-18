package ru.vldkrt.imagesearch.data.entities

import com.google.gson.annotations.SerializedName
import ru.vldkrt.imagesearch.domain.entities.ImageResult

data class ImageResultRemote(

    @field:SerializedName("is_product")
    val isProduct: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String,

    @field:SerializedName("original")
    val original: String,

    @field:SerializedName("link")
    val link: String,

    @field:SerializedName("position")
    val position: Int,

    @field:SerializedName("source")
    val source: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("in_stock")
    val inStock: String? = null,
)

// Mapper functions (Remote <-> Domain). Also can be written as regular class like Mapper<I, O>, but
// extensions make it easier and let us avoid another package with mappers.
// All kept inside the one class in this implementation.
// In our case Domain -> Remote is not needed - this is just for demo how we can handle mapping in
// both ways.

fun ImageResultRemote.asDomain() =
    ImageResult(isProduct, thumbnail, original, link, position, source, title, inStock)

fun ImageResult.asRemote() =
    ImageResultRemote(isProduct, thumbnail, original, link, position, source, title, inStock)