package ru.vldkrt.imagesearch.business.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vldkrt.imagesearch.data.api.LOAD_SIZE
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.domain.entities.ImageResultData
import ru.vldkrt.imagesearch.domain.usecases.GetImagesUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
) : ViewModel() {

    private val imageFactory = ImagePagingFactory(getImagesUseCase)

    val query get() = imageFactory.query

    private val searchAction = MutableSharedFlow<String>()

    fun setQuery(query: String?) {
        imageFactory.query = query?.trim().orEmpty()
    }

    fun search() {
        viewModelScope.launch {
            searchAction.emit(imageFactory.query)
        }
    }

    // Images fetched only by submitting a query
    val images: Flow<PagingData<ImageResult>> = searchAction
        .flatMapLatest {
            Pager(
                PagingConfig(pageSize = LOAD_SIZE, enablePlaceholders = false),
                pagingSourceFactory = { imageFactory }
            ).flow
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
        .cachedIn(viewModelScope)
}