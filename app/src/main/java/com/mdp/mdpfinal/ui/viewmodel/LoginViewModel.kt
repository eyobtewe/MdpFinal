package com.mdp.mdpfinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdp.mdpfinal.data.local.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthState {
    LOADING,
    LOGGED_IN,
    LOGGED_OUT
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState = _authState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // .first() gets the first emitted value and suspends until it's available
            val isLoggedIn = userPreferencesRepository.isLoggedIn.first()
            _authState.value = if (isLoggedIn) AuthState.LOGGED_IN else AuthState.LOGGED_OUT
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            userPreferencesRepository.setLoggedIn(true)
            _authState.value = AuthState.LOGGED_IN
        }
    }
}
    