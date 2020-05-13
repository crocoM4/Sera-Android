package com.guerra.enrico.login.di

import com.guerra.enrico.base.di.FeatureScope
import com.guerra.enrico.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by enrico
 * on 13/05/2020.
 */
@Module
abstract class LoginBindingModule {
  @FeatureScope
  @ContributesAndroidInjector(
    modules = [LoginModule::class]
  )
  internal abstract fun loginActivity(): LoginActivity
}