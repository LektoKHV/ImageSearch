package ru.vldkrt.imagesearch.business.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.vldkrt.imagesearch.domain.entities.ImageResult
import ru.vldkrt.imagesearch.entities.asUI

class FragmentPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private val list: MutableList<ImageResult> = arrayListOf()

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        val image = list[position]
        return ImageFragment().apply {
            arguments = ImageFragmentArgs.Builder(image.asUI(), image.title).build().toBundle()
        }
    }

    fun updateList(newList: List<ImageResult>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}