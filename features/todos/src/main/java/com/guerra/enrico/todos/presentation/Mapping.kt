package com.guerra.enrico.todos.presentation

import com.guerra.enrico.models.todos.Category
import com.guerra.enrico.models.todos.Task

/**
 * Created by enrico
 * on 29/12/2019.
 */
fun categoriesToPresentations(categories: List<Category>): List<CategoryPresentation> =
  categories.map {
    CategoryPresentation(
      category = it,
      isChecked = false
    )
  }

fun tasksToPresentations(tasks: List<Task>): List<TaskPresentation> =
  tasks.map { taskToPresentations(it) }

fun taskToPresentations(tasks: Task): TaskPresentation =
  TaskPresentation(
    task = tasks,
    isExpanded = false
  )