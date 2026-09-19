package com.clinic.patientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clinic.patientapp.theme.ClinicPatientAppTheme
import com.clinic.patientapp.ui.PatientViewModel
import com.clinic.patientapp.ui.PaymentScreen
import com.clinic.patientapp.ui.QueueTrackerScreen
import com.clinic.patientapp.ui.auth.LoginScreen
import com.clinic.patientapp.ui.auth.SignUpScreen
import com.clinic.patientapp.ui.main.DoctorProfileScreen
import com.clinic.patientapp.ui.main.HomeScreen
import com.clinic.patientapp.ui.main.HomeViewModel

class MainActivity : ComponentActivity() {
    private val patientViewModel: PatientViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicPatientAppTheme {
                val navController = rememberNavController()
                AppNavHost(navController, patientViewModel, homeViewModel)
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    patientViewModel: PatientViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("home") {
            // Load doctors when home is reached
            homeViewModel.fetchDoctors()
            val doctors by homeViewModel.doctors.collectAsState()
            
            HomeScreen(
                doctors = doctors,
                onDoctorSelected = { doctorId -> 
                    navController.navigate("doctor_profile/$doctorId")
                }
            )
        }
        composable("doctor_profile/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            val doctors by homeViewModel.doctors.collectAsState()
            val selectedDoctor = doctors.find { it.id == doctorId }
            
            if (selectedDoctor != null) {
                DoctorProfileScreen(
                    doctor = selectedDoctor,
                    viewModel = patientViewModel,
                    onBookShift = { shiftId -> 
                        patientViewModel.registerForShift("mock-patient-id", shiftId)
                        navController.navigate("payment/mock_visit_id")
                    }
                )
            }
        }
        composable("payment/{visitId}") { backStackEntry ->
            val visitId = backStackEntry.arguments?.getString("visitId") ?: ""
            PaymentScreen(
                visitId = visitId,
                viewModel = patientViewModel,
                onPaymentSubmitted = { navController.navigate("queue_tracker") }
            )
        }
        composable("queue_tracker") {
            QueueTrackerScreen(viewModel = patientViewModel)
        }
    }
}
