package ru.vldkrt.imagesearch.data.entities

import com.google.gson.annotations.SerializedName
import ru.vldkrt.imagesearch.domain.entities.ImageResultData

data class ImageResultDataRemote(

	@field:SerializedName("images_results")
	val imagesResults: List<ImageResultRemote> = listOf()
)

fun ImageResultDataRemote.asDomain() = ImageResultData(imagesResults.map { it.asDomain() })
fun ImageResultData.asRemote() = ImageResultDataRemote(imagesResults.map { it.asRemote() })