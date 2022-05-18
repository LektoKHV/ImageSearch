package ru.vldkrt.imagesearch.util

// Used for navigation in this project, it prevents crash when trying to navigate from
// wrong (old) navigation destination.
inline fun runSafely(noinline error: ((Exception) -> Unit)? = null, block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        error?.invoke(e)
    }
}