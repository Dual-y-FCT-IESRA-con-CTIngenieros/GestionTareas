package com.es.appmovil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.es.appmovil.data.remote.RetrofitClient
import com.es.appmovil.data.repository.AuthRepository
import com.es.appmovil.data.repository.TimeRecordRepository
import com.es.appmovil.ui.login.LoginScreen
import com.es.appmovil.ui.login.LoginViewModel
import com.es.appmovil.ui.mytime.MyTimeScreen
import com.es.appmovil.ui.mytime.MyTimeViewModel
import com.es.appmovil.utils.TokenManager

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val apiService = remember { RetrofitClient.create(tokenManager) }
    val authRepository = remember { AuthRepository(apiService) }
    val timeRecordRepository = remember { TimeRecordRepository(apiService) }

    val navController = rememberNavController()
    val startDestination = if (tokenManager.getToken() != null) "mytime" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            val vm: LoginViewModel = viewModel {
                LoginViewModel(tokenManager, authRepository)
            }
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = {
                    navController.navigate("mytime") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("mytime") {
            val vm: MyTimeViewModel = viewModel {
                MyTimeViewModel(tokenManager, timeRecordRepository)
            }
            MyTimeScreen(
                viewModel = vm,
                onLogout = {
                    tokenManager.clearToken()
                    tokenManager.clearUser()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

