package com.guerra.enrico.sera.di.module

import android.app.Application
import android.content.Context
import com.guerra.enrico.base.dispatcher.CoroutineContextProvider
import com.guerra.enrico.base.dispatcher.CoroutineContextProviderImpl
import com.guerra.enrico.sera.SeraApplication
import com.guerra.enrico.data.local.db.LocalDbManager
import com.guerra.enrico.data.local.db.LocalDbManagerImpl
import com.guerra.enrico.data.local.db.SeraDatabase
import com.guerra.enrico.data.local.prefs.PreferencesManager
import com.guerra.enrico.data.local.prefs.PreferencesManagerImpl
import com.guerra.enrico.data.remote.RemoteDataManager
import com.guerra.enrico.data.remote.RemoteDataManagerImpl
import com.guerra.enrico.sera.data.repo.auth.AuthRepository
import com.guerra.enrico.sera.data.repo.auth.AuthRepositoryImpl
import com.guerra.enrico.sera.data.repo.category.CategoryRepository
import com.guerra.enrico.sera.data.repo.category.CategoryRepositoryImpl
import com.guerra.enrico.sera.data.repo.task.TaskRepository
import com.guerra.enrico.sera.data.repo.task.TaskRepositoryImpl
import com.guerra.enrico.base.scheduler.SchedulerProvider
import com.guerra.enrico.base.scheduler.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by enrico
 * on 30/05/2018.
 */
@Module(includes = [ViewModelModule::class])
class AppModule {
  @Provides
  fun provideContext(application: SeraApplication): Context = application.applicationContext

  @Provides
  fun provideApplication(application: SeraApplication): Application = application

  @Provides
  @Singleton
  fun provideSeraDatabase(context: Context): SeraDatabase = SeraDatabase.getInstance(context)

  @Provides
  fun provideCompositeDisposable() = CompositeDisposable()

  @Provides
  @Singleton
  fun provideSchedulerProvider(schedulerProvider: SchedulerProviderImpl): SchedulerProvider = schedulerProvider

  @Provides
  @Singleton
  fun provideAppDispatchers(appDispatchers: CoroutineContextProviderImpl): CoroutineContextProvider = appDispatchers

  @Provides
  @Singleton
  fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository = authRepository

  @Provides
  @Singleton
  fun provideCategoryRepository(categoryRepository: CategoryRepositoryImpl): CategoryRepository = categoryRepository

  @Provides
  @Singleton
  fun provideTaskRepository(taskRepository: TaskRepositoryImpl): TaskRepository = taskRepository

  @Provides
  @Singleton
  fun provideLocalDbManager(localDbManager: LocalDbManagerImpl): LocalDbManager = localDbManager

  @Provides
  @Singleton
  fun providePreferencesManager(preferencesManager: PreferencesManagerImpl): PreferencesManager = preferencesManager

  @Provides
  @Singleton
  fun provideRemoteDataManager(remoteDataManager: RemoteDataManagerImpl): RemoteDataManager = remoteDataManager
}