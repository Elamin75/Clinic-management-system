package com.clinic.patientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clinic.patientapp.ui.BookingScreen
import com.clinic.patientapp.ui.PaymentScreen
import com.clinic.patientapp.ui.QueueTrackerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    PatientAppNavigation()
                }
            }
        }
    }
}

@Composable
fun PatientAppNavigation() {
    val navController = rememberNavController()
    val viewModel: PatientViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    
    NavHost(navController = navController, startDestination = "booking") {
        composable("booking") {
            BookingScreen(
                viewModel = viewModel,
                onShiftSelected = { shiftId ->
                    navController.navigate("payment/$shiftId")
                }
            )
        }
        composable("payment/{visitId}") { backStackEntry ->
            val visitId = backStackEntry.arguments?.getString("visitId") ?: ""
            PaymentScreen(
                visitId = visitId,
                viewModel = viewModel,
                onPaymentUploaded = {
                    navController.navigate("queueTracker/$visitId") {
                        popUpTo("booking") { inclusive = false }
                    }
                }
            )
        }
        composable("queueTracker/{visitId}") { backStackEntry ->
            val visitId = backStackEntry.arguments?.getString("visitId") ?: ""
            QueueTrackerScreen(visitId = visitId)
        }
    }
}
