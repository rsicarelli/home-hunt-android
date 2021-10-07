package com.rsicarelli.homehunt.presentation.resources

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.futured.hauler.setOnDragDismissedListener
import coil.ImageLoader
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.databinding.VideoPlayerActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


internal const val KEY_ARG_VIDEO_URL = "KEY_ARG_VIDEO_URL"

fun launchVideoPlayerActivity(context: Context, videoUrl: String) {
    context.startActivity(createVideoPlayerActivityIntent(context, videoUrl))
}

@VisibleForTesting
fun createVideoPlayerActivityIntent(context: Context, videoUrl: String): Intent {
    return Intent(context, VideoPlayerActivity::class.java).apply {
        putExtra(KEY_ARG_VIDEO_URL, videoUrl)
    }
}


@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var binding: VideoPlayerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = VideoPlayerActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val videoUrl = intent.getStringExtra(KEY_ARG_VIDEO_URL)!!

        val vimeoVideo = convertVideoUrl(videoUrl)

        with(binding.webView) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    webView: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    webView.loadUrl(request.url.toString())
                    return true
                }
            }
            settings.javaScriptEnabled = true

            loadData(vimeoVideo, "text/html", "utf-8")
        }

        binding.heulerView.setOnDragDismissedListener {
            finish()
        }
    }

    private fun convertVideoUrl(videoUrl: String): String {
        val sizes: Pair<Int, Int> = if (isLandscape()) {
            Pair(800, 300)
        } else {
            Pair(380, 300)
        }

        val (width, height) = sizes

        return "<html><body leftmargin=\"0\" topmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\"><iframe src=\"${videoUrl}\" width=\"${width}\" height=\"${height}\" frameborder=\"0\" allowfullscreen=true></iframe></body></html>"
    }
}

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
