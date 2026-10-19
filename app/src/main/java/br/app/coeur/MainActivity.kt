package br.app.coeur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.app.coeur.ui.demo.MaterialDemoScreen
import br.app.coeur.ui.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var loggedIn by remember { mutableStateOf(false) }
                    if (loggedIn) {
                        MaterialDemoScreen(onLogout = { loggedIn = false })
                    } else {
                        LoginScreen(onLoginSuccess = { loggedIn = true })
                    }
                }
            }
        }
    }
}
