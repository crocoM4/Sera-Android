package com.guerra.enrico.todos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guerra.enrico.models.todos.Category
import com.guerra.enrico.sera.databinding.ItemSimpleCategoryBinding

/**
 * Created by enrico
 * on 04/01/2020.
 */
class SimpleCategoryAdapter : RecyclerView.Adapter<_root_ide_package_.com.guerra.enrico.todos.adapter.SimpleCategoryViewHolder>() {
  var categories = emptyList<Category>()

  override fun getItemCount(): Int = categories.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    _root_ide_package_.com.guerra.enrico.todos.adapter.SimpleCategoryViewHolder(
      ItemSimpleCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

  override fun onBindViewHolder(holder: _root_ide_package_.com.guerra.enrico.todos.adapter.SimpleCategoryViewHolder, position: Int) {
    holder.bind(categories[position])
  }
}

class SimpleCategoryViewHolder(private val binding: ItemSimpleCategoryBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(category: Category) {
    binding.category = category
    binding.executePendingBindings()
  }
}