package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.ar.sceneform.rendering.PlaneRenderer
import com.google.ar.sceneform.ux.ArFragment
import dev.anacoimbra.androidaugmentedreality.helpers.loadRenderable
import kotlinx.android.synthetic.main.activity_ar.*

class ObjectPlacementActivity : BaseArActivity() {

    override val arFragment = ArFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment {
            setupPlaneDiscovery()
            setupPlaneTap()
            setupUpdate()
        }

        loadRenderable(this, onSuccess = ::renderable.setter)
    }

    private fun setupPlaneDiscovery() {
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(instructionView)
        arFragment.planeDiscoveryController.show()
    }

    private fun setupPlaneTap() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            if (!hasRendered()) return@setOnTapArPlaneListener

            placeNode(hitResult.createAnchor())
            arFragment.setOnTapArPlaneListener(null)
        }
    }

    private fun setupUpdate() {
        arFragment.arSceneView.scene.addOnUpdateListener {
            arFragment.arSceneView.planeRenderer.material
                .thenAccept { material ->
                    material.setFloat(PlaneRenderer.MATERIAL_SPOTLIGHT_RADIUS, 100f)
                }
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ObjectPlacementActivity::class.java))
    }
}
