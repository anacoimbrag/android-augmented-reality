package dev.anacoimbra.androidaugmentedreality.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.ArCoreApk
import dev.anacoimbra.androidaugmentedreality.MainAdapter
import dev.anacoimbra.androidaugmentedreality.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainItems.adapter = MainAdapter()
        maybeEnableButtons()
    }

    private fun maybeEnableButtons() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient)
            Handler().postDelayed({ maybeEnableButtons() }, 200)

        if (availability.isSupported) mainItems.visibility = View.VISIBLE
        else mainItems.visibility = View.GONE
    }
}
