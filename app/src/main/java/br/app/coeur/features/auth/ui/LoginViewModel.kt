package br.app.coeur.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.app.coeur.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email, errorMessage = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password, errorMessage = null
            )
        }
    }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Preencha email e senha."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true, errorMessage = null
            )
        }

        viewModelScope.launch {
            val result = authRepository.login(state.email, state.password)

            _uiState.update { currentState ->
                result.fold(
                    onSuccess = { currentState.copy(isLoading = false, isLoggedIn = true) },
                    onFailure = { error ->
                        currentState.copy(
                            isLoading = false, errorMessage = error.message ?: "Falha ao entrar."
                        )
                    })
            }
        }
    }
}
