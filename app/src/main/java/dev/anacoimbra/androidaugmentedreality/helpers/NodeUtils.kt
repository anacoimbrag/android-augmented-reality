package dev.anacoimbra.androidaugmentedreality.helpers

import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem

fun Scene.createAnchor(anchor: Anchor) =
    AnchorNode(anchor).apply {
        setParent(this@createAnchor)
    }

fun TransformationSystem.createNode(
    parent: Node,
    renderable: ModelRenderable,
    setRenderable: Boolean = true,
    select: Boolean = true
) = TransformableNode(this).apply {
    setParent(parent)
    if (setRenderable)
        this.renderable = renderable
    if (select)
        select()
}