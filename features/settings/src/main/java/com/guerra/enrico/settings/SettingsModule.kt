package com.guerra.enrico.settings

import androidx.lifecycle.ViewModel
import com.guerra.enrico.base.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by enrico
 * on 08/03/2020.
 */

@Module
abstract class SettingsModule {
  @Binds
  @IntoMap
  @ViewModelKey(SettingsViewModel::class)
  abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}