package ru.vldkrt.imagesearch.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.data.api.ImageClient
import ru.vldkrt.imagesearch.data.entities.asDomain
import ru.vldkrt.imagesearch.data.networkBoundResource
import ru.vldkrt.imagesearch.domain.entities.ImageResultData
import ru.vldkrt.imagesearch.domain.repositories.ImageRepository

class ImageRepositoryImpl(private val client: ImageClient) : ImageRepository {

    override fun getImages(query: String, page: Int): Flow<Resource<ImageResultData>> = networkBoundResource(
        remoteCall = { client.getImages(query = query, page = page) },
        mapper = { it.asDomain() }
    )
}