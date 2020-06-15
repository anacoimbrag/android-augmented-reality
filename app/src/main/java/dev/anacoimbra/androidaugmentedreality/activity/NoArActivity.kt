package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformationSystem
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.helpers.createNode
import dev.anacoimbra.androidaugmentedreality.helpers.loadRenderable
import dev.anacoimbra.androidaugmentedreality.helpers.screenShot
import kotlinx.android.synthetic.main.activity_no_ar.*
import kotlinx.android.synthetic.main.controls.*


class NoArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_ar)

        loadRenderable(this, onSuccess = {
            addNodeToScene(it)
        })

        setupControls()
    }

    override fun onResume() {
        super.onResume()
        sceneView.resume()
    }

    override fun onPause() {
        super.onPause()
        sceneView.pause()
    }

    private fun setupControls() {
        btnClose.setIconTintResource(R.color.primaryTextColor)
        btnClose.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            sceneView.screenShot()
        }
    }

    private fun addNodeToScene(model: ModelRenderable?) {
        val ts = TransformationSystem(resources.displayMetrics, FootprintSelectionVisualizer())
        model?.let {
            ts.createNode(sceneView.scene, model).apply {
                localPosition = Vector3(0f, 0f, -2f)
                localRotation =
                    Quaternion.lookRotation(Vector3(0.5f, 0f, -0.5f), Vector3(0.5f, 0f, -0.5f))
                localScale = Vector3(0.8f, 0.8f, 0.8f)
                name = "Fox"
            }
        }

        sceneView.scene.addOnPeekTouchListener { hitTestResult, motionEvent ->
            try {
                ts.onTouch(hitTestResult, motionEvent)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, NoArActivity::class.java))
    }
}
