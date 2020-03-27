package dev.anacoimbra.androidaugmentedreality.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.ar.core.Plane
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.helpers.loadRenderable

class ObjectPlacementActivity : BaseArActivity() {

    override val arFragment = ArFragment()

    private val node = Node()
    private lateinit var floorRenderable: ModelRenderable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment {
            setupPlaneTap()
            setupUpdate()
        }

        loadModel()
        loadRenderable(this, onSuccess = ::renderable.setter)
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
            if (!::floorRenderable.isInitialized) return@addOnUpdateListener
            val frame = arFragment.arSceneView.arFrame ?: return@addOnUpdateListener
            val planes = frame.getUpdatedTrackables(Plane::class.java)
            planes.forEach { plane ->
                val pose = plane.centerPose
                node.localPosition = Vector3(pose.tx(), pose.ty(), pose.tz())

                node.renderable = floorRenderable
                node.setParent(arFragment.arSceneView.scene)
            }
        }
    }

    private fun loadModel() {
        val sampler =
            Texture.Sampler.builder()
                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                .build()
        Texture.builder()
            .setSource(this, R.drawable.target)
            .setSampler(sampler)
            .build()
            .thenAccept { texture ->
                MaterialFactory.makeOpaqueWithTexture(this, texture)
                    .thenAccept { material ->
                        floorRenderable =
                            ShapeFactory.makeCylinder(0.1f, 0.00001f, Vector3(0f, 0f, 0f), material)
                    }
            }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ObjectPlacementActivity::class.java))
    }
}
