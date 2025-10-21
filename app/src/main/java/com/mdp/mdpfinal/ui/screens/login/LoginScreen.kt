package com.mdp.mdpfinal.ui.screens.login

import androidx.compose.foundation.layout.*    import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mdp.mdpfinal.navigation.Screen
import com.mdp.mdpfinal.ui.theme.MdpFinalTheme
import com.mdp.mdpfinal.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
//    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Observe the login state from the ViewModel
//    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // If logged in, navigate to Home and remove Login from the back stack
//    LaunchedEffect(isLoggedIn) {
//        if (isLoggedIn) {
//            navController.navigate(Screen.Home.route) {
//                popUpTo(Screen.Login.route) { inclusive = true }
//            }
//        }
//    }

    // The content of the screen will be displayed only if not logged in
//    if (!isLoggedIn) {
        LoginContent(onLoginClick = {
            // Trigger the login logic in the ViewModel
            viewModel.onLoginClicked()
        })
//    }
}

@Composable
fun LoginContent(onLoginClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotBlank() && password.isNotBlank() // Basic validation
        ) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MdpFinalTheme {
        LoginContent(onLoginClick = {})
    }
}
    