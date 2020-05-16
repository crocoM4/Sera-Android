package com.guerra.enrico.login.di

import com.guerra.enrico.base.di.FeatureScope
import com.guerra.enrico.domain.DomainModule
import com.guerra.enrico.login.LoginActivity
import com.guerra.enrico.navigation.di.ActivityDestination
import com.guerra.enrico.navigation.di.ActivityKey
import com.guerra.enrico.navigation.di.DestinationInfo
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by enrico
 * on 13/05/2020.
 */
@Module(includes = [DomainModule::class, LoginNavigationModule::class])
abstract class LoginBindingModule {
  @FeatureScope
  @ContributesAndroidInjector(
    modules = [LoginModule::class]
  )
  internal abstract fun loginActivity(): LoginActivity
}

@Module
class LoginNavigationModule {
  @Provides
  @IntoMap
  @ActivityKey(ActivityDestination.LOGIN)
  internal fun provideLoginDestination(): DestinationInfo {
    return DestinationInfo(LoginActivity::class.java.simpleName)
  }
}