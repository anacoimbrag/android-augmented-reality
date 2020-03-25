package dev.anacoimbra.androidaugmentedreality.helpers

import android.content.SharedPreferences
import org.koin.java.KoinJavaComponent.inject

object AnchorManager {
    private const val NEXT_SHORT_CODE = "next_short_code"
    private const val KEY_PREFIX = "anchor-"
    private const val INITIAL_CODE = 0
    private val pref: SharedPreferences by inject(SharedPreferences::class.java)

    private fun nextCode(): Int {
        val lastCode = pref.getInt(
            NEXT_SHORT_CODE,
            INITIAL_CODE
        )
        pref[NEXT_SHORT_CODE] = lastCode + 1
        return lastCode
    }

    fun saveCloudAnchorId(cloudAnchorId: String): Int {
        val nextCode =
            nextCode()
        pref[KEY_PREFIX + nextCode] = cloudAnchorId
        return nextCode
    }

    fun getCloudAnchorId(code: String) = pref.get<String>(
        KEY_PREFIX + code)
}