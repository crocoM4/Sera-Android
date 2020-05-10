package com.guerra.enrico.goals

import androidx.lifecycle.ViewModel
import com.guerra.enrico.base.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by enrico
 * on 18/08/2018.
 */
@Module
abstract class GoalsModule {
  @Binds
  @IntoMap
  @ViewModelKey(GoalsViewModel::class)
  abstract fun bindGoalsViewModel(viewModel: GoalsViewModel): ViewModel
}