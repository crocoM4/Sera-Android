package com.guerra.enrico.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.guerra.enrico.base.extensions.observeEvent
import com.guerra.enrico.base.extensions.setLightStatusBarIfNeeded
import com.guerra.enrico.base.extensions.systemUiFullScreen
import com.guerra.enrico.base_android.arch.BaseActivity
import com.guerra.enrico.login.databinding.ActivityLoginBinding
import com.guerra.enrico.login.models.Step
import com.guerra.enrico.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
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

  private lateinit var binding: ActivityLoginBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setLightStatusBarIfNeeded()

    binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    binding.root.systemUiFullScreen()

    observeEvent(viewModel.step) {
      val target = when (it) {
        Step.LOGIN -> LoginNavigationRoutes.Form.buildTarget()
        Step.SYNC -> LoginNavigationRoutes.Sync.buildTarget()
      }
      navigator.replaceFragment(supportFragmentManager, R.id.loginFragmentHost, target)
    }
  }
}