package com.guerra.enrico.navigation

import android.net.Uri

interface Navigator {
  fun getUriMain(): Uri
  fun getUriTodos(): Uri
  fun getUriGoals(): Uri
  fun getUriResults(): Uri
  fun getUriSettings(): Uri
}