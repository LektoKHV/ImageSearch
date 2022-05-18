package ru.vldkrt.imagesearch.domain.usecases

import ru.vldkrt.imagesearch.domain.repositories.ImageRepository

class GetImagesUseCase(
    private val imageRepository: ImageRepository,
) {

    operator fun invoke(query: String, page: Int) = imageRepository.getImages(query, page)
}