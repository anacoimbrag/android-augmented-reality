package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.fragment.AugmentedImageFragment
import dev.anacoimbra.androidaugmentedreality.helpers.loadRenderable

class AugmentedImageActivity : BaseArActivity() {

    override val arFragment = AugmentedImageFragment()
    private var isRendered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment {
            setupUpdate()
        }
        loadRenderable(this, onSuccess = ::renderable.setter)
    }

    private fun setupUpdate() {
        arFragment.arSceneView.scene.addOnUpdateListener {
            val frame = arFragment.arSceneView.arFrame ?: return@addOnUpdateListener

            val augmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
            augmentedImages.forEach {
                checkImage(it)
            }
        }
    }

    private fun checkImage(image: AugmentedImage) {
        when (image.trackingState!!) {
            TrackingState.PAUSED -> {
                showMessage(getString(R.string.image_detected_placeholder, image.name))
            }
            TrackingState.TRACKING -> {
                if (!isRendered) {
                    isRendered = true
                    setImage(image)
                }
            }
            TrackingState.STOPPED -> {
                isRendered = false
            }
        }
    }

    private fun setImage(image: AugmentedImage) {
        if (!hasRendered()) return

        placeNode(image.createAnchor(image.centerPose))
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, AugmentedImageActivity::class.java))
    }
}
