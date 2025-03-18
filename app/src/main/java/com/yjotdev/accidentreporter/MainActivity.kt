package com.yjotdev.accidentreporter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel
import com.yjotdev.accidentreporter.application.navigation.PermissionView
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //Inicializa el viewmodel
            val viewModel: AppViewModel = hiltViewModel()
            //Carga la informacion
            viewModel.loadData()
            AccidentReporterTheme {
                PermissionView(viewModel = viewModel)
            }
        }
    }
}