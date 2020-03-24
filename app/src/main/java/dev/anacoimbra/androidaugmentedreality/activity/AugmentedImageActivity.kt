package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.fragment.AugmentedImageFragment

class AugmentedImageActivity : AppCompatActivity() {

    private val arFragment = AugmentedImageFragment()
    private lateinit var renderable: ModelRenderable
    private var isRendered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_ar, arFragment).commit()
        renderModel()
        setupUpdate()
    }

    private fun renderModel() {
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder()
                    .setSource(this, Uri.parse("fox/fox.gltf"), RenderableSource.SourceType.GLTF2)
                    .setScale(0.05f)
                    .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                    .build()
            )
            .setRegistryId("Fox")
            .build()
            .thenAccept { renderable ->
                this.renderable = renderable
            }
            .exceptionally { error ->
                Log.e(TAG, error.localizedMessage, error)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.error_loading_model,
                    Snackbar.LENGTH_SHORT
                ).show()
                return@exceptionally null
            }
    }

    private fun setupUpdate() {
        if (arFragment.arSceneView != null) {
            arFragment.arSceneView.scene.addOnUpdateListener {
                val frame = arFragment.arSceneView.arFrame ?: return@addOnUpdateListener

                val augmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
                augmentedImages.forEach {
                    when (it.trackingState!!) {
                        TrackingState.PAUSED -> {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                getString(R.string.image_detected_placeholder, it.name),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        TrackingState.TRACKING -> {
                            if (!isRendered) {
                                isRendered = true
                                setImage(it)
                            }
                        }
                        TrackingState.STOPPED -> {
                            isRendered = false
                        }
                    }
                }
            }
        } else {
            Handler().postDelayed({ setupUpdate() }, 300)
        }
    }

    private fun setImage(image: AugmentedImage) {
        if (!::renderable.isInitialized) return

        val anchor = image.createAnchor(image.centerPose)
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)
        createTransformableNode(anchorNode)
    }

    private fun createTransformableNode(anchorNode: AnchorNode) =
        TransformableNode(arFragment.transformationSystem).apply {
            setParent(anchorNode)
            renderable = this@AugmentedImageActivity.renderable
            select()
        }

    companion object {
        private const val TAG = "Augmented Image"

        fun start(context: Context) =
            context.startActivity(Intent(context, AugmentedImageActivity::class.java))
    }
}
