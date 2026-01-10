package com.yjotdev.accidentreporter

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.accidentreporter.application.navigation.PermissionView
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ajusta la vista a toda la pantalla
        viewEdgeToEdge()
        // Configura la IU de la actividad
        setContent {
            AccidentReporterTheme {
                PermissionView()
            }
        }
    }

    private fun viewEdgeToEdge(){
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }
    }
}