package ru.vldkrt.imagesearch.business.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.entities.ImageResultUI
import ru.vldkrt.imagesearch.entities.asDomain

class ImagePagerViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentItem: ImageResult? = savedStateHandle.get<ImageResultUI>("image")?.asDomain()
}