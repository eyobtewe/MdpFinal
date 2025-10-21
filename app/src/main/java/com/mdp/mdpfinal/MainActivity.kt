package com.mdp.mdpfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mdp.mdpfinal.navigation.AppNavigation // Import your NavHost
import com.mdp.mdpfinal.ui.theme.MdpFinalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // <-- Add this annotation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MdpFinalTheme {
                // Replace Scaffold and Greeting with your AppNavigation
                AppNavigation()
            }
        }
    }
}
    