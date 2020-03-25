package dev.anacoimbra.androidaugmentedreality.fragment

import android.util.Log
import android.widget.Toast
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.*
import com.google.ar.sceneform.ux.BaseArFragment
import dev.anacoimbra.androidaugmentedreality.R

class CloudAnchorFragment : BaseArFragment() {
    override fun getSessionConfiguration(session: Session?): Config =
        Config(session).apply {
            cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        }

    override fun getSessionFeatures(): MutableSet<Session.Feature> = mutableSetOf()

    override fun handleSessionException(sessionException: UnavailableException?) {
        val message = when (sessionException) {
            is UnavailableArcoreNotInstalledException -> R.string.error_install_arcore
            is UnavailableApkTooOldException -> R.string.error_update_arcore
            is UnavailableSdkTooOldException -> R.string.error_update_app
            is UnavailableDeviceNotCompatibleException -> R.string.error_unsupported_device
            else -> R.string.error_generic_arcore
        }
        Log.e("CloudAnchorFragment", "Error: $message", sessionException)
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun getAdditionalPermissions(): Array<String> = emptyArray()

    override fun isArRequired(): Boolean = true
}