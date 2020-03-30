package dev.anacoimbra.androidaugmentedreality.helpers

import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem

fun Scene.createAnchor(anchor: Anchor) =
    AnchorNode(anchor).apply {
        setParent(this@createAnchor)
    }

fun TransformationSystem.createNode(
    parent: NodeParent,
    renderable: ModelRenderable,
    scale: Float = 0.1f,
    setRenderable: Boolean = true,
    select: Boolean = true
) = TransformableNode(this).apply {
    setParent(parent)
    if (setRenderable)
        this.renderable = renderable
    if (select)
        select()

    this.scaleController.minScale = 0.01f
    this.scaleController.maxScale = 3.0f
    this.scaleController.sensitivity = 1.5f

    localScale = Vector3(scale, scale, scale)
}