package com.guerra.enrico.sera.data.repo.todos.task

import com.guerra.enrico.base.Result
import com.guerra.enrico.local.db.LocalDbManager
import com.guerra.enrico.models.sync.Operation
import com.guerra.enrico.models.sync.SyncAction
import com.guerra.enrico.models.todos.Category
import com.guerra.enrico.models.todos.Task
import com.guerra.enrico.remote.RemoteDataManager
import com.guerra.enrico.remote.response.CallResult
import com.guerra.enrico.remote.response.toRemoteExceptionOrUnknown
import com.guerra.enrico.sera.data.repo.withAccessToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

/**
 * Created by enrico
 * on 21/08/2018.
 */
class TaskRepositoryImpl @Inject constructor(
  private val localDbManager: LocalDbManager,
  private val remoteDataManager: RemoteDataManager
) : TaskRepository {

  override suspend fun pullTasks(from: Date?): Result<Unit> = localDbManager.withAccessToken {
    return@withAccessToken when (val apiResult = remoteDataManager.getTasks(it, from)) {
      is CallResult.Result -> {
        val data = apiResult.apiResponse.data
        if (apiResult.apiResponse.success && data != null) {
          localDbManager.insertTasks(data)
          Result.Success(Unit)
        } else {
          Result.Error(apiResult.apiResponse.error.toRemoteExceptionOrUnknown())
        }
      }
      is CallResult.Error -> {
        Result.Error(apiResult.exception)
      }
    }
  }

  override suspend fun insertTask(task: Task): Result<Task> {
    localDbManager.insertTask(task)
    localDbManager.insertSyncAction(task.toSyncAction(Operation.INSERT))
    return Result.Success(task)
  }

  override suspend fun deleteTask(task: Task): Result<Int> {
    val result = localDbManager.deleteTask(task)
    localDbManager.insertSyncAction(task.toSyncAction(Operation.DELETE))
    return Result.Success(result)
  }

  override suspend fun updateTask(task: Task): Result<Task> {
    localDbManager.updateTask(task)
    localDbManager.insertSyncAction(task.toSyncAction(Operation.UPDATE))
    return Result.Success(task)
  }

  override fun getTasks(
    searchText: String,
    category: Category?,
    completed: Boolean
  ): Flow<List<Task>> =
    localDbManager.observeTasks(completed).map { list ->
      val regex = """(?=.*$searchText)""".toRegex(RegexOption.IGNORE_CASE)
      if (searchText.isNotEmpty()) {
        return@map list.filter { c -> c.description.contains(regex) }
      }
      if (category !== null) {
        return@map list.filter { t -> t.categories.any { c -> category.id == c.id } }
      }
      return@map list
    }

  override suspend fun syncAction(syncAction: SyncAction): Result<Any> =
    localDbManager.withAccessToken {
      val task = localDbManager.getTask(syncAction.entityId)

      return@withAccessToken when (syncAction.operation) {
        Operation.INSERT -> updateSyncedTask(remoteDataManager.insertTask(it, task))
        Operation.UPDATE -> updateSyncedTask(remoteDataManager.updateTask(it, task))
        Operation.DELETE -> remoteDataManager.deleteTask(it, syncAction.entityId).toResult()
      }
    }

  private suspend fun updateSyncedTask(callResult: CallResult<Task>): Result<Task> {
    val result = callResult.toResult()
    if (result is Result.Success) {
      localDbManager.updateTask(result.data)
    }
    return result
  }
}