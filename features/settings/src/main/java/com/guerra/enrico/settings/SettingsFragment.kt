package com.guerra.enrico.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.guerra.enrico.base.extensions.launchWhenResumed
import com.guerra.enrico.base.extensions.observe
import com.guerra.enrico.base.extensions.observeEvent
import com.guerra.enrico.base_android.arch.BaseFragment
import com.guerra.enrico.settings.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.delay

/**
 * Created by enrico
 * on 08/03/2020.
 */
@AndroidEntryPoint
internal class SettingsFragment : BaseFragment() {
  private val viewModel: SettingsViewModel by viewModels()

  private lateinit var binding: FragmentSettingsBinding
  private lateinit var settingAdapter: SettingAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
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
    binding.toolbar.toolbarTitle.text = getString(R.string.title_settings)
    initRecyclerView()
    observe(viewModel.list) {
      settingAdapter.submitList(it)
    }
    observeEvent(viewModel.enableDarkTheme) {
      launchWhenResumed {
        delay(400)
        if (it) {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
      }
    }
  }

  private fun initRecyclerView() {
    settingAdapter = SettingAdapter(viewLifecycleOwner, viewModel)
    val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    binding.recyclerViewSettings.apply {
      layoutManager = linearLayoutManager
      adapter = settingAdapter
      (itemAnimator as DefaultItemAnimator).run {
        supportsChangeAnimations = false
        addDuration = 160L
        moveDuration = 160L
        changeDuration = 160L
        removeDuration = 120L
      }
      addItemDecoration(
        DividerItemDecoration(
          requireContext(),
          DividerItemDecoration.VERTICAL
        ).apply {
          setDrawable(requireContext().getDrawable(R.drawable.line_item_divider) ?: return)
        })
    }
  }
}