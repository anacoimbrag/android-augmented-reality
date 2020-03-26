package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.ar.sceneform.ux.ArFragment
import dev.anacoimbra.androidaugmentedreality.helpers.loadRenderable

class ObjectPlacementActivity : BaseArActivity() {

    override val arFragment = ArFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment {
            setupPlaneTap()
        }

        loadRenderable(this, onSuccess = ::renderable.setter)
    }

    private fun setupPlaneTap() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            if (!hasRendered()) return@setOnTapArPlaneListener

            placeNode(hitResult.createAnchor())
            arFragment.setOnTapArPlaneListener(null)
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ObjectPlacementActivity::class.java))
    }
}
