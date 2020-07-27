package com.guerra.enrico.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.guerra.enrico.base.extensions.observeEvent
import com.guerra.enrico.base.extensions.setLightStatusBarIfNeeded
import com.guerra.enrico.base.extensions.systemUiFullScreen
import com.guerra.enrico.base_android.arch.BaseActivity
import com.guerra.enrico.login.models.Step
import com.guerra.enrico.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * Created by enrico
 * on 12/10/2018.
 */

@AndroidEntryPoint
internal class LoginActivity : BaseActivity() {
  private val viewModel: LoginViewModel by viewModels()

  @Inject
  lateinit var navigator: Navigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setLightStatusBarIfNeeded()

    setContentView(R.layout.activity_login)
    root.systemUiFullScreen()

    observeEvent(viewModel.step) {
      when (it) {
        Step.LOGIN -> findNavController(R.id.loginFragmentHost).navigate(R.id.loginFragment)
        Step.SYNC -> findNavController(R.id.loginFragmentHost).navigate(R.id.syncFragment)
        Step.COMPLETE -> {
          val uri = navigator.getUriTodos(this)
          findNavController(R.id.loginFragmentHost).navigate(uri)
        }
      }
    }
  }
}