package com.guerra.enrico.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import com.guerra.enrico.base_android.arch.BaseFragment
import com.guerra.enrico.goals.databinding.FragmentGoalsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_goals.view.*

/**
 * Created by enrico
 * on 27/05/2018.
 */
@AndroidEntryPoint
internal class GoalsFragment : BaseFragment() {

  private val viewModel: GoalsViewModel by viewModels()

  private lateinit var binding: FragmentGoalsBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentGoalsBinding.inflate(inflater, container, false).apply {
      lifecycleOwner = viewLifecycleOwner
    }
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enterTransition = MaterialFadeThrough()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView()
  }

  private fun initView() {
    binding.toolbar.toolbarTitle.text = resources.getString(R.string.title_goals)
    val messageResources = com.guerra.enrico.base_android.exception.MessageExceptionManager(
      Exception()
    ).getResources()
    binding.messageLayout.apply {
      setImage(messageResources.icon)
      setMessage(messageResources.message)
      setButton(resources.getString(R.string.message_layout_button_try_again)) {}
      show()
    }
  }
}