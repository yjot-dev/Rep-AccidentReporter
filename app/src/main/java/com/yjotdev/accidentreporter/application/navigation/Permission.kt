package com.yjotdev.accidentreporter.application.navigation

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel

@Composable
fun PermissionView(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasPermissions by remember{ mutableStateOf(checkPermissions(context)) }
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        hasPermissions = permissions.all { permission -> permission.value }
    }
    if(hasPermissions){
        //Navegacion
        NavigationView(
            navController = navController,
            viewModel = viewModel
        )
    }else{
        // Solicitar permisos al usuario
        LaunchedEffect(Unit){
            requestPermissionsLauncher.launch(getPermission())
        }
    }
}

/** Verifica los permisos **/
private fun checkPermissions(context: Context): Boolean {
    return getPermission().all { permission ->
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}

/** Obtiene los permisos **/
private fun getPermission() : Array<String>{
    val permissionArray = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    return permissionArray
}