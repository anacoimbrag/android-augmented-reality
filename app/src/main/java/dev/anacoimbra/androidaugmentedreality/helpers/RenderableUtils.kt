package dev.anacoimbra.androidaugmentedreality.helpers

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import dev.anacoimbra.androidaugmentedreality.R
import dev.anacoimbra.androidaugmentedreality.activity.BaseArActivity

fun loadRenderable(
    activity: Activity,
    file: String = "fox/fox.gltf",
    scale: Float = 0.5f,
    onSuccess: (renderable: ModelRenderable) -> Unit
) {
    ModelRenderable.builder()
        .setSource(
            activity, RenderableSource.builder()
                .setSource(
                    activity,
                    Uri.parse(file),
                    RenderableSource.SourceType.GLTF2
                )
                .setScale(scale)
                .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                .build()
        )
        .setRegistryId("Fox")
        .build()
        .thenAccept { renderable ->
            onSuccess.invoke(renderable)
        }
        .exceptionally { error ->
            Log.e("Load Renderable", error.localizedMessage, error)
            if (activity is BaseArActivity)
                activity.findViewById<TextView>(R.id.txtMessage)
                    ?.setText(R.string.error_loading_model)
            return@exceptionally null
        }
}