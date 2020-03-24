package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem
import dev.anacoimbra.androidaugmentedreality.R
import kotlinx.android.synthetic.main.activity_no_ar.*


class NoArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_ar)

        renderModel()
    }

    override fun onResume() {
        super.onResume()
        sceneView.resume()
    }

    override fun onPause() {
        super.onPause()
        sceneView.pause()
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
                addNodeToScene(renderable)
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

    private fun addNodeToScene(model: ModelRenderable?) {
        val transformationSystem =
            TransformationSystem(resources.displayMetrics, FootprintSelectionVisualizer())
        model?.let {
            val node = TransformableNode(transformationSystem).apply {
                setParent(scene)
                localPosition = Vector3(0f, 0f, -2f)
                localRotation =
                    Quaternion.lookRotation(Vector3(0.5f, 0f, -0.5f), Vector3(0.5f, 0f, -0.5f))
                localScale = Vector3(0.8f, 0.8f, 0.8f)
                name = "Fox"
                renderable = it
                select()
            }

            sceneView.scene.addChild(node)
        }

        sceneView.scene.addOnPeekTouchListener { hitTestResult, motionEvent ->
            try {
                transformationSystem.onTouch(hitTestResult, motionEvent)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "No AR"

        fun start(context: Context) =
            context.startActivity(Intent(context, NoArActivity::class.java))
    }
}
