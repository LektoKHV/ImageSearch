package ru.vldkrt.imagesearch.view

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    @DimenRes private val spacing: Int,
    private val headerNum: Int,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacingPx = parent.resources.getDimension(spacing).toInt()
        val position = parent.getChildAdapterPosition(view) - headerNum // item position

        if (position >= 0) {
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacingPx - column * spacingPx / spanCount // spacingPx - column * ((1f / spanCount) * spacingPx)
                outRect.right = (column + 1) * spacingPx / spanCount // (column + 1) * ((1f / spanCount) * spacingPx)

                if (position < spanCount) outRect.top = spacingPx // top edge

                outRect.bottom = spacingPx // item bottom
            } else {
                outRect.left = column * spacingPx / spanCount // column * ((1f / spanCount) * spacingPx)
                outRect.right = spacingPx - (column + 1) * spacingPx / spanCount // spacingPx - (column + 1) * ((1f /    spanCount) * spacingPx)
                if (position >= spanCount) outRect.top = spacingPx // item top
            }
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }
}