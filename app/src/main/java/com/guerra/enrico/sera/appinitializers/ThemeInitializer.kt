package com.guerra.enrico.sera.appinitializers

import android.app.Application
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate
import com.guerra.enrico.base.appinitializers.AppInitializer
import com.guerra.enrico.domain.interactors.settings.Settings
import javax.inject.Inject

class ThemeInitializer @Inject constructor(
  private val settings: Settings
) : AppInitializer {
  override fun init(application: Application) {
    val darkTheme = settings.getDarkTheme()
    val currentMode =
      application.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    if (darkTheme.active && currentMode == UI_MODE_NIGHT_NO ) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
  }
}