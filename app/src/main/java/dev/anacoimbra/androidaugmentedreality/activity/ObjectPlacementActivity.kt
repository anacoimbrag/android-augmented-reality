package dev.anacoimbra.androidaugmentedreality.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import dev.anacoimbra.androidaugmentedreality.R

class ObjectPlacementActivity : AppCompatActivity() {

    private val arFragment = ArFragment()
    private lateinit var renderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this))
            return

        setContentView(R.layout.activity_ar)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_ar, arFragment).commit()

        renderModel()
        setupPlaneTap()
    }

    private fun renderModel() {
        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder()
                    .setSource(this, Uri.parse("fox/fox.gltf"), RenderableSource.SourceType.GLTF2)
                    .setScale(0.3f)
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

    private fun setupPlaneTap() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            if (!::renderable.isInitialized) return@setOnTapArPlaneListener

            val anchorNode = createAnchorNode(hitResult)
            createTransformableNode(anchorNode)
            arFragment.setOnTapArPlaneListener(null)
        }
    }

    private fun createAnchorNode(hitResult: HitResult) =
        AnchorNode(hitResult.createAnchor()).apply {
            setParent(arFragment.arSceneView.scene)
        }

    private fun createTransformableNode(anchorNode: AnchorNode) =
        TransformableNode(arFragment.transformationSystem).apply {
            setParent(anchorNode)
            renderable = this@ObjectPlacementActivity.renderable
            select()
        }

    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(
                activity,
                R.string.error_opengl_min_version,
                Toast.LENGTH_LONG
            ).show()
            activity.finish()
            return false
        }
        return true
    }

    companion object {
        private const val MIN_OPENGL_VERSION = 3.0
        private const val TAG = "Object Placement"

        fun start(context: Context) =
            context.startActivity(Intent(context, ObjectPlacementActivity::class.java))
    }
}
