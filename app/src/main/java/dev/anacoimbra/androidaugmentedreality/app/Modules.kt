package dev.anacoimbra.androidaugmentedreality.app

import dev.anacoimbra.androidaugmentedreality.helpers.defaultPref
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { defaultPref(androidContext()) }
}