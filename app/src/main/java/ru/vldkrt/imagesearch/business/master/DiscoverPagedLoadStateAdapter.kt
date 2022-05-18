package ru.vldkrt.imagesearch.business.master

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vldkrt.imagesearch.R
import ru.vldkrt.imagesearch.databinding.ItemLoadStateFooterBinding

class DiscoverPagedLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<DiscoverPagedLoadStateAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder.create(parent, retry)
    }


    class ViewHolder(
        private val binding: ItemLoadStateFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.also {
                it.setOnClickListener { retry.invoke() }
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.message
            }
            binding.progressBar.visibility =
                if (loadState is LoadState.Loading) View.VISIBLE else View.INVISIBLE

            val isErrorVisible = if (loadState is LoadState.Error) View.VISIBLE else View.INVISIBLE
            binding.retryButton.visibility = isErrorVisible
            binding.errorMsg.visibility = isErrorVisible
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_state_footer, parent, false)
                val binding = ItemLoadStateFooterBinding.bind(view)
                return ViewHolder(binding, retry)
            }
        }
    }
}