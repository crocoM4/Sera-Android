package com.guerra.enrico.domain.interactors

import com.guerra.enrico.base.Result
import com.guerra.enrico.base.dispatcher.IODispatcher
import com.guerra.enrico.domain.Interactor
import com.guerra.enrico.models.User
import com.guerra.enrico.sera.data.repo.auth.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by enrico
 * on 12/11/2019.
 */
class SignIn @Inject constructor(
  private val authRepository: AuthRepository,
  @IODispatcher override val dispatcher: CoroutineDispatcher
) : Interactor<String, Result<User>>() {

  override suspend fun doWork(params: String): Result<User> {
    return authRepository.googleSignInCallback(params)
  }
}