package com.guerra.enrico.base_android.arch

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.guerra.enrico.base.Event
import com.guerra.enrico.base.extensions.isNotNullAndEmpty
import com.guerra.enrico.base.extensions.onDismiss
import com.guerra.enrico.base_android.R
import com.guerra.enrico.base_android.extensions.viewLifecycleScope
import com.guerra.enrico.components.OverlayLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by enrico
 * on 18/08/2018.
 */
open class BaseFragment : Fragment {
  private var snackbar: Snackbar? = null
  private lateinit var overlayLoader: OverlayLoader

  constructor() : super()
  constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    overlayLoader =
      OverlayLoader.make(requireActivity(), resources.getString(R.string.label_loading))
  }

  fun <T> observe(flow: Flow<T>, block: (T) -> Unit) {
    flow
      .onEach { block(it) }
      .launchIn(viewLifecycleScope)
  }

  fun <T> observeEvent(flow: Flow<Event<T>>, block: (T) -> Unit) {
    flow
      .onEach {
        val event = it.getContent() ?: return@onEach
        block(event)
      }
      .launchIn(viewLifecycleScope)
  }

  fun observeLoading(flow: Flow<Event<Boolean>>) {
    flow
      .onEach { event ->
        val show = event.getContent() ?: return@onEach
        if (show) {
          showOverlayLoader()
        } else {
          hideOverlayLoader()
        }
      }
      .launchIn(viewLifecycleScope)
  }

  fun showSnackbar(
    @StringRes messageId: Int, view: View? = null, @StringRes actionId: Int? = null,
    onAction: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null
  ) {
    if (!isAdded) return
    val actionText = if (actionId != null) resources.getString(actionId) else null
    showSnackbar(resources.getString(messageId), view, actionText, onAction, onDismiss)
  }

  fun showSnackbar(
    message: String,
    view: View? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
  ) {
    if (!isAdded || message.isEmpty()) return
    snackbar = Snackbar.make(
      view ?: requireActivity().findViewById(android.R.id.content),
      message,
      Snackbar.LENGTH_LONG
    )
    if (actionText.isNotNullAndEmpty() && onAction != null) {
      snackbar?.setAction(actionText) { onAction() }
    }
    if (onDismiss != null) {
      snackbar?.onDismiss(onDismiss)
    }
    snackbar?.show()
  }

  fun showOverlayLoader() {
    if (!isAdded) return
    overlayLoader.show()
  }

  fun hideOverlayLoader() {
    if (!isAdded) return
    overlayLoader.hide()
  }

  override fun onStop() {
    snackbar?.let {
      if (it.isShownOrQueued) {
        it.dismiss()
      }
    }
    snackbar = null
    super.onStop()
  }
}