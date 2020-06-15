package dev.anacoimbra.androidaugmentedreality.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.*
import com.google.ar.sceneform.ux.BaseArFragment
import dev.anacoimbra.androidaugmentedreality.R
import java.io.IOException


class AugmentedImageFragment : BaseArFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false

        return view
    }

    override fun getSessionConfiguration(session: Session?): Config =
        Config(session).setAugmentedImageDatabase(AugmentedImageDatabase(session).apply {
            addImage("fox", "images/fox.jpg".assetToBitmap(), 0.1f)
        })

    override fun getSessionFeatures(): MutableSet<Session.Feature> = mutableSetOf()

    override fun handleSessionException(sessionException: UnavailableException?) {
        val message = when (sessionException) {
            is UnavailableArcoreNotInstalledException -> R.string.error_install_arcore
            is UnavailableApkTooOldException -> R.string.error_update_arcore
            is UnavailableSdkTooOldException -> R.string.error_update_app
            is UnavailableDeviceNotCompatibleException -> R.string.error_unsupported_device
            else -> R.string.error_generic_arcore
        }
        Log.e(TAG, "Error: $message", sessionException)
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun getAdditionalPermissions(): Array<String> = emptyArray()

    override fun isArRequired(): Boolean = true

    private fun String.assetToBitmap(): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            requireContext().assets.open(this)
                .use { inputStream -> bitmap = BitmapFactory.decodeStream(inputStream) }
        } catch (e: IOException) {
            Log.e(TAG, "I/O exception loading augmented image bitmap.", e)
        }
        return bitmap
    }

    companion object {
        private const val TAG = "AugmentedImageFragment"
    }
}