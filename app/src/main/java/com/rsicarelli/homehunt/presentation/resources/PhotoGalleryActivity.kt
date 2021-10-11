package com.rsicarelli.homehunt.presentation.resources

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import app.futured.hauler.setOnDragDismissedListener
import coil.ImageLoader
import coil.request.ImageRequest
import com.ortiz.touchview.TouchImageView
import com.rsicarelli.homehunt.databinding.PhotoGalleryActivityBinding
import com.rsicarelli.homehunt.domain.model.Property
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal const val KEY_ARG_DETAILS_PHOTO_GALLERY = "KEY_ARG_DETAILS_PHOTO_GALLERY"

fun launchPhotoGalleryActivity(context: Context, property: Property) {
    context.startActivity(createPhotoGalleryActivityIntent(context, property))
}

@VisibleForTesting
fun createPhotoGalleryActivityIntent(context: Context, property: Property): Intent {
    val intent = Intent(context, PhotoGalleryActivity::class.java)
    intent.putStringArrayListExtra(
        KEY_ARG_DETAILS_PHOTO_GALLERY,
        ArrayList(property.photoGalleryUrls)
    )
    return intent
}


//TODO refactor to Compose§
@AndroidEntryPoint
class PhotoGalleryActivity : AppCompatActivity() {

    private lateinit var binding: PhotoGalleryActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageLoader = ImageLoader.Builder(this)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()

        binding = PhotoGalleryActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val photoImages = intent.getStringArrayListExtra(KEY_ARG_DETAILS_PHOTO_GALLERY)

        binding.viewPager2.adapter =
            AdapterImages(
                photoList = photoImages!!.toList(),
                context = this,
                imageLoader = imageLoader,
                coroutinesScope = lifecycleScope
            )

        binding.heulerView.setOnDragDismissedListener {
            finish()
        }
    }

}

class AdapterImages(
    private val photoList: List<String>,
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val coroutinesScope: CoroutineScope
) :
    RecyclerView.Adapter<AdapterImages.ViewHolder>() {

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(view: TouchImageView) : RecyclerView.ViewHolder(view) {
        val imagePlace = view
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TouchImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            setOnTouchListener { view, event ->
                var result = true
                //can scroll horizontally checks if there's still a part of the image
                //that can be scrolled until you reach the edge
                if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && canScrollHorizontally(
                        -1
                    )
                ) {
                    //multi-touch event
                    result = when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            // Disallow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(true)
                            // Disable touch on view
                            false
                        }
                        MotionEvent.ACTION_UP -> {
                            // Allow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                        else -> true
                    }
                }
                result
            }
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = ImageRequest.Builder(context)
            .data(photoList[position])
            .build()
        coroutinesScope.launch {
            val drawable = imageLoader.execute(request).drawable
            holder.imagePlace.setImageDrawable(drawable)
        }
    }

    override fun getItemViewType(i: Int): Int {
        return 0
    }

}