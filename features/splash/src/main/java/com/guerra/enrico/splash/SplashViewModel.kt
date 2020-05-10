package com.guerra.enrico.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.guerra.enrico.base.Result
import com.guerra.enrico.domain.interactors.todos.ValidateToken
import com.guerra.enrico.domain.invoke
import com.guerra.enrico.models.User
import com.guerra.enrico.base_android.arch.BaseViewModel
import javax.inject.Inject

/**
 * Created by enrico
 * on 17/10/2018.
 */
class SplashViewModel @Inject constructor(
  validateToken: ValidateToken
) : BaseViewModel() {
  val validationAccessTokenResult: LiveData<Result<User>> = liveData {
    emit(Result.Loading)
    emit(validateToken())
  }
}