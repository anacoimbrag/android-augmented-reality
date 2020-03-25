package dev.anacoimbra.androidaugmentedreality

import android.content.Context
import android.content.SharedPreferences

fun defaultPref(context: Context): SharedPreferences =
    context.getSharedPreferences("dev.anacoimbra.preferences", Context.MODE_PRIVATE)

operator fun SharedPreferences.set(key: String, value: Any?) =
    when (value) {
        is String? -> edit { putString(key, value) }
        is Int -> edit { putInt(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }

inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? =
    when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }

private inline fun SharedPreferences.edit(operation: SharedPreferences.Editor.() -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}