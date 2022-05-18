package ru.vldkrt.imagesearch.view

import android.view.View

fun interface OnItemClickListener<T> {
    fun onItemClick(view: View, item: T)
}