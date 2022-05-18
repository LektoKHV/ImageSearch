package ru.vldkrt.imagesearch.domain.entities

data class ImageResult(

    val isProduct: String? = null,

    val thumbnail: String,

    val original: String,

    val link: String,

    val position: Int,

    val source: String,

    val title: String,

    val inStock: String? = null
)