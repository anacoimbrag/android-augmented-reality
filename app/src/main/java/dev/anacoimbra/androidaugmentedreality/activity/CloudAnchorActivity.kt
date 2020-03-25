package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import dev.anacoimbra.androidaugmentedreality.helpers.AnchorManager
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.fragment.CloudAnchorFragment
import kotlinx.android.synthetic.main.activity_ar.*
import kotlinx.android.synthetic.main.cloud_anchor.*
import kotlinx.android.synthetic.main.cloud_anchor.view.*

class CloudAnchorActivity : AppCompatActivity() {

    enum class AnchorState {
        NONE,
        HOSTING,
        HOSTED,
        RESOLVING,
        RESOLVED
    }

    private val arFragment = CloudAnchorFragment()
    private var cloudAnchor: Anchor? = null
    private lateinit var renderable: ModelRenderable

    private var state = AnchorState.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ar)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_ar, arFragment).commit()

        renderModel()

        val view = layoutInflater.inflate(R.layout.cloud_anchor, container)

        view.btnClear.setOnClickListener {
            clearCloudAnchor()
        }

        view.btnRetrieve.setOnClickListener {
            resolveCloudAnchor()
        }

        Handler().postDelayed({
            hostCloudAnchor()
            arFragment.arSceneView.scene.addOnUpdateListener {
                checkState()
            }
        }, 300)
    }

    private fun cloudAnchor(newAnchor: Anchor?) {
        cloudAnchor?.detach()
        cloudAnchor = newAnchor
        state = AnchorState.NONE
    }

    private fun hostCloudAnchor() {
        arFragment.setOnTapArPlaneListener { hitResult, plane, _ ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING)
                return@setOnTapArPlaneListener

            val anchor = arFragment.arSceneView.session?.hostCloudAnchor(hitResult.createAnchor())
            cloudAnchor(anchor)
            state = AnchorState.HOSTING
            showMessage(R.string.message_hosting_anchor)
            val anchorNode = createAnchorNode(hitResult)
            createTransformableNode(anchorNode)
        }
    }

    private fun resolveCloudAnchor() {
        if (cloudAnchor != null) {
            showMessage(R.string.error_clear_anchor)
            return
        }

        buildDialog()
    }

    private fun buildDialog() {
        val editText = AppCompatEditText(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.title_resolve_anchor)
            .setMessage(R.string.text_resolve_anchor)
            .setView(editText)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                startResolving(editText.text.toString())
                dialog.dismiss()
            }.show()
    }

    private fun startResolving(code: String) {
        val cloudId = AnchorManager.getCloudAnchorId(code)
        val resolvedCloudAnchor = arFragment.arSceneView.session?.resolveCloudAnchor(cloudId)
        cloudAnchor(resolvedCloudAnchor)
        state = AnchorState.RESOLVING
        showMessage(R.string.message_resolving_anchor)
        createTransformableNode(AnchorNode(resolvedCloudAnchor).apply { setParent(arFragment.arSceneView.scene) })
    }

    private fun clearCloudAnchor() {
        cloudAnchor(null)
        txtMessage.visibility = View.GONE
    }

    private fun checkState() {
        val anchorState = cloudAnchor?.cloudAnchorState
        when (state) {
            AnchorState.RESOLVING -> {
                if (anchorState?.isError == true) {
                    showMessage(R.string.error_resolved_anchor)
                } else if (anchorState == Anchor.CloudAnchorState.SUCCESS) {
                    state = AnchorState.RESOLVED
                    showMessage(R.string.message_resolved_anchor)
                }
            }
            AnchorState.HOSTING -> {
                if (anchorState?.isError == true) {
                    showMessage(R.string.error_hosted_anchor)
                } else if (anchorState == Anchor.CloudAnchorState.SUCCESS) {
                    val id = cloudAnchor?.cloudAnchorId ?: return
                    val code = AnchorManager.saveCloudAnchorId(id)
                    state = AnchorState.HOSTED
                    showMessage(getString(R.string.message_host_anchor, code))
                }
            }
            else -> return
        }
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

    private fun createAnchorNode(hitResult: HitResult) =
        AnchorNode(hitResult.createAnchor()).apply {
            setParent(arFragment.arSceneView.scene)
        }

    private fun createTransformableNode(anchorNode: AnchorNode) =
        TransformableNode(arFragment.transformationSystem).apply {
            setParent(anchorNode)
            renderable = this@CloudAnchorActivity.renderable
            select()
        }

    private fun showMessage(@StringRes message: Int) {
        txtMessage.visibility = View.VISIBLE
        txtMessage.setText(message)
    }

    private fun showMessage(message: String) {
        txtMessage.visibility = View.VISIBLE
        txtMessage.text = message
    }

    companion object {
        private const val TAG = "Cloud Anchor"
        fun start(context: Context) =
            context.startActivity(Intent(context, CloudAnchorActivity::class.java))
    }
}