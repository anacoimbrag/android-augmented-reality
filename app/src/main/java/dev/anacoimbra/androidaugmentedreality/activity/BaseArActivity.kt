package dev.anacoimbra.androidaugmentedreality.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.BaseArFragment
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.helpers.createAnchor
import dev.anacoimbra.androidaugmentedreality.helpers.createNode
import kotlinx.android.synthetic.main.controls.*

@SuppressLint("Registered")
abstract class BaseArActivity :
    AppCompatActivity() {

    abstract val arFragment: BaseArFragment
    lateinit var renderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        setupControls()
    }

    private fun setupControls() {
        btnClose.setOnClickListener {
            finish()
        }

        btnShare.setOnClickListener {
            //TODO: share functionality
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
}