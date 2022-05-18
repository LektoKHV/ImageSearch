package ru.vldkrt.imagesearch.business.web

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ru.vldkrt.imagesearch.databinding.FragmentWebBinding

class WebImageFragment : Fragment() {

    private val args: WebImageFragmentArgs by navArgs()
    private var _binding: FragmentWebBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            // Disallow redirecting to browser
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    progressBar.isVisible = false
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            }

            // Changing behavior of back button: turn page back instead of popping the fragment
            webView.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) { // If key pressed
                    if (keyCode == KeyEvent.KEYCODE_BACK) { // If it's back button
                        if (webView.canGoBack()) {
                            webView.goBack()
                            return@setOnKeyListener true
                        }
                    }
                }
                return@setOnKeyListener false
            }

            webView.settings.javaScriptEnabled = true

            // Load the start page
            webView.loadUrl(args.image.link)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}