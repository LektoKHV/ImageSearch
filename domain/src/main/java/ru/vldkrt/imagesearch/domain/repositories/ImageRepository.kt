package ru.vldkrt.imagesearch.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.domain.entities.ImageResultData

interface ImageRepository {

    fun getImages(query: String, page: Int): Flow<Resource<ImageResultData>>
}