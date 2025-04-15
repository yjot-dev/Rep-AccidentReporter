package com.yjotdev.accidentreporter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.accidentreporter.application.navigation.PermissionView
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!isRunningTest()){
            setContent {
                AccidentReporterTheme {
                    PermissionView()
                }
            }
        }
    }

    private fun isRunningTest(): Boolean {
        return BuildConfig.DEBUG && Thread.currentThread().stackTrace.any {
            it.className.contains("androidx.test")
        }
    }
}