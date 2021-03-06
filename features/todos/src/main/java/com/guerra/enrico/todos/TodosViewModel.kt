package com.guerra.enrico.todos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.guerra.enrico.base.Result
import com.guerra.enrico.base.dispatcher.IODispatcher
import com.guerra.enrico.base.extensions.event
import com.guerra.enrico.base_android.arch.viewmodel.SingleStateViewModel
import com.guerra.enrico.domain.interactors.todos.UpdateTaskCompleteState
import com.guerra.enrico.domain.invoke
import com.guerra.enrico.domain.observers.todos.ObserveCategories
import com.guerra.enrico.domain.observers.todos.ObserveTasks
import com.guerra.enrico.models.todos.Task
import com.guerra.enrico.navigation.models.todos.SearchData
import com.guerra.enrico.todos.models.SnackbarEvent
import com.guerra.enrico.todos.models.TodosEvent
import com.guerra.enrico.todos.models.TodosState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TodosViewModel @ViewModelInject constructor(
  @IODispatcher dispatcher: CoroutineDispatcher,
  private val reducer: TodosReducer,
  observeCategories: ObserveCategories,
  private val observeTasks: ObserveTasks,
  private val updateTaskCompleteState: UpdateTaskCompleteState
) : SingleStateViewModel<TodosState, TodosEvent>(
  initialState = TodosState.Idle, dispatcher = dispatcher
) {

  init {
    observeTasks.observe()
      .onEach { tasks ->
        state = reducer.updateWithTasks(state, tasks)
      }
      .launchIn(viewModelScope)
    observeCategories.observe()
      .onEach { categories ->
        state = reducer.updateWithCategories(state, categories)
      }
      .launchIn(viewModelScope)

    observeTasks(ObserveTasks.Params())
    observeCategories()
  }

  fun onSearchResult(searchData: SearchData) {
    val text = searchData.text
    when {
      searchData.category != null ->
        observeTasks(ObserveTasks.Params(category = searchData.category))
      text != null && text.isNotBlank() ->
        observeTasks(ObserveTasks.Params(text = text))
      searchData.suggestion != null ->
        observeTasks(ObserveTasks.Params(suggestion = searchData.suggestion))
    }
  }

  fun onTaskClick(task: Task) {

  }

  fun onTaskSwipeToComplete(task: Task) = runIf<TodosState.Data> { data ->
    state = reducer.addPendingCompletedTask(data, task)
    eventsChannel.event = TodosEvent.ShowSnackbar(SnackbarEvent.UndoCompleteTask(
      onAction = { undoCompletedTask(task) },
      onDismiss = { commitPendingCompletedTasks() }
    ))
  }

  private fun commitPendingCompletedTasks(): Unit = runIf<TodosState.Data> { data ->
    viewModelScope.launch {
      withContext(NonCancellable) {
        val actions = data.pendingCompletedTasks.map { task ->
          async {
            updateTaskCompleteState(UpdateTaskCompleteState.Params(task, completed = true))
          }
        }

        val results = actions.awaitAll()

        val errorResult = results.firstOrNull { it is Result.Error }
        if (errorResult != null && errorResult is Result.Error) {
          eventsChannel.event =
            TodosEvent.ShowSnackbar(SnackbarEvent.Message(errorResult.exception))
        }
      }
    }
  }

  private fun undoCompletedTask(task: Task) = runIf<TodosState.Data> { data ->
    state = reducer.removePendingCompletedTask(data, task)
  }
}