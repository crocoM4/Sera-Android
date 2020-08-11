package com.guerra.enrico.settings

import androidx.hilt.lifecycle.ViewModelInject
import com.guerra.enrico.base.Event
import com.guerra.enrico.base.dispatcher.IODispatcher
import com.guerra.enrico.base_android.arch.viewmodel.SingleStateViewModel
import com.guerra.enrico.domain.interactors.settings.Settings
import com.guerra.enrico.models.Setting
import com.guerra.enrico.settings.models.SettingsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

internal class SettingsViewModel @ViewModelInject constructor(
  @IODispatcher dispatcher: CoroutineDispatcher,
  private val settings: Settings
) : SingleStateViewModel<SettingsState>(initialState = SettingsState.Idle, dispatcher = dispatcher),
  EventActions {

  private val _enableDarkTheme = ConflatedBroadcastChannel<Event<Boolean>>()
  val enableDarkTheme: Flow<Event<Boolean>>
    get() = _enableDarkTheme.asFlow().onEach { delay(400) }

  init {
    getList()
  }

  private fun getList() {
    state = SettingsState.Items(settings.getSettings())
  }

  override fun onSettingClick(setting: Setting) {
    when (setting) {
      is Setting.DarkTheme -> {
        val newDarkThemeState = !setting.active

        _enableDarkTheme.offer(Event(newDarkThemeState))
        settings.updateDarkTheme(newDarkThemeState)
      }
    }
    getList()
  }

  override fun onCleared() {
    super.onCleared()
    _enableDarkTheme.close()
  }

}