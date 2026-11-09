package br.app.coeur.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.app.coeur.modules.auth.presentation.LoginScreen
import br.app.coeur.shared.ui.demo.MaterialDemoScreen

@Composable
fun CoeurNavigation() {
    var loggedIn by remember { mutableStateOf(false) }
    if (loggedIn) {
        MaterialDemoScreen(onLogout = { loggedIn = false })
    } else {
        LoginScreen(onLoginSuccess = { loggedIn = true })
    }
}
