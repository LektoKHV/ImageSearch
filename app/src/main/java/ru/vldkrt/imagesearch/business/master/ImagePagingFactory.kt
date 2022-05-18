package ru.vldkrt.imagesearch.business.master

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.toList
import retrofit2.HttpException
import ru.vldkrt.imagesearch.data.api.LOAD_SIZE
import ru.vldkrt.imagesearch.domain.Resource
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.domain.usecases.GetImagesUseCase
import java.io.IOException
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 0

class ImagePagingFactory @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
) : PagingSource<Int, ImageResult>() {

    var query: String = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResult> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val pageSize = params.loadSize.coerceAtMost(LOAD_SIZE)

        return try {
            val imageDataFlow = getImagesUseCase(query, position)
            // Get the last flow value
            val imagesResource: Resource<List<ImageResult>> =
                imageDataFlow.toList().last().map { it.imagesResults }

            // There's something strange with SerpAPI, it returns 116 elements on ijn=0 page for q=Apple. Cut the trail.
            val imagesTrimmed: Resource<List<ImageResult>> =
                imagesResource.map { it.take(LOAD_SIZE) }

            // If data was not loaded, assign error message
            if (imagesTrimmed is Resource.Error) {
                return LoadResult.Error(Exception(imagesTrimmed.errorMessage))
            }

            val imageList: List<ImageResult>? = imagesTrimmed.data
            val nextKey = if (imageList == null || imageList.size < pageSize) {
                null
            } else {
                position + 1
            }
            val prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1

            LoadResult.Page(
                data = imageList ?: listOf(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, ImageResult>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}