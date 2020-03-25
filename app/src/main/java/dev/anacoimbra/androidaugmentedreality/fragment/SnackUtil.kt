package dev.anacoimbra.androidaugmentedreality.fragment

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.anacoimbra.androidaugmentedreality.R

object SnackUtil {

    private enum class DismissBehavior {
        HIDE, SHOW, FINISH
    }

    fun showMessage(view: View, message: String) =
        show(view, message, DismissBehavior.HIDE)

    fun showMessage(view: View, @StringRes message: Int) =
        show(view, view.context.getString(message), DismissBehavior.HIDE)

    fun showMessageWithDismiss(view: View, @StringRes message: Int) =
        show(view, view.context.getString(message), DismissBehavior.SHOW)

    fun showError(view: View, @StringRes message: Int) =
        show(view, view.context.getString(message), DismissBehavior.FINISH)

    private fun show(
        view: View,
        message: String,
        dismissBehavior: DismissBehavior
    ) {
        val snackbar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_INDEFINITE
        )

        if (dismissBehavior != DismissBehavior.HIDE)
            snackbar.setAction(R.string.close) { _ ->
                snackbar.dismiss()
            }
        if (dismissBehavior == DismissBehavior.FINISH)
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    (view.context as? Activity)?.finish()
                }
            })

        snackbar.show()
    }
}