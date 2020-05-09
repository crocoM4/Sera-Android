package com.guerra.enrico.todos.add.steps

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.guerra.enrico.base.Result
import com.guerra.enrico.base.extensions.observe
import com.guerra.enrico.base.succeeded
import com.guerra.enrico.sera.R
import com.guerra.enrico.sera.ui.base.BaseFragment
import com.guerra.enrico.todos.add.TodoAddViewModel
import kotlinx.android.synthetic.main.fragment_todo_add_schedule.*
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by enrico
 * on 21/10/2018.
 */
class ScheduleFragment : BaseFragment() {
  private lateinit var root: WeakReference<View>

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel: TodoAddViewModel by activityViewModels { viewModelFactory }

  private var selectedDate = Date()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_todo_add_schedule, container, false)
    root = WeakReference(view)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    observeCreateTask()
    scheduleDate.setOnClickListener {
      showDatePicker()
    }
    buttonAdd.setOnClickListener {
      viewModel.onAddTask(selectedDate)
    }
  }

  private fun observeCreateTask() {
    observe(viewModel.createdTaskResult) { result ->
      if (result is Result.Loading) {
        showOverlayLoader()
        return@observe
      }
      hideOverlayLoader()
      if (result.succeeded) {
        viewModel.goToNextStep(StepEnum.DONE)
      }
      if (result is Result.Error) {
        root.get()?.let {
          showSnackbar(
            result.exception.message
              ?: "An error occur while creating the task", it
          )
        }
      }

    }
  }

  private fun showDatePicker() {
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val day = today.get(Calendar.DAY_OF_MONTH)
    val mContext = context ?: return
    val datePickerDialog =
      DatePickerDialog(mContext, OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.MONTH, selectedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        scheduleDate.setText(sdf.format(calendar.time))
        selectedDate = calendar.time
      }, year, month, day)
    datePickerDialog.show()
  }
}