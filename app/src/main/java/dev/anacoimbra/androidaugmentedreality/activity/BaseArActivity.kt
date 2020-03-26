package dev.anacoimbra.androidaugmentedreality.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.BaseArFragment
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.helpers.createAnchor
import dev.anacoimbra.androidaugmentedreality.helpers.createNode
import dev.anacoimbra.androidaugmentedreality.helpers.screenShot
import kotlinx.android.synthetic.main.controls.*

@SuppressLint("Registered")
abstract class BaseArActivity : AppCompatActivity() {

    abstract val arFragment: BaseArFragment
    lateinit var renderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this))
            return
        setContentView(R.layout.activity_ar)

        setupControls()
    }

    private fun setupControls() {
        btnClose.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            arFragment.arSceneView.screenShot()
        }
    }

    protected fun replaceFragment(onCommit: (() -> Unit)? = null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_ar, arFragment)
            .runOnCommit {
                onCommit?.invoke()
            }
            .commit()
    }

    protected fun hasRendered() = ::renderable.isInitialized

    protected fun placeNode(anchor: Anchor) {
        val anchorNode = arFragment.arSceneView.scene.createAnchor(anchor)
        arFragment.transformationSystem.createNode(anchorNode, renderable)
    }

    protected fun showMessage(@StringRes message: Int) {
        txtMessage.visibility = View.VISIBLE
        txtMessage.setText(message)
    }

    protected fun showMessage(message: String) {
        txtMessage.visibility = View.VISIBLE
        txtMessage.text = message
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
        private const val TAG = "AR Activity"
    }
}