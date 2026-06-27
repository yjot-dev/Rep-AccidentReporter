package com.yjotdev.accidentreporter.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.navigation.ViewRoutes
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.presentation.utils.ComponentPreview

const val BACK_BUTTON = "backbutton"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(
    viewRoutes: ViewRoutes,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
){
    if(canNavigateBack) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = viewRoutes.idTitle),
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                        contentDescription = BACK_BUTTON,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewTitleBar(){
    AccidentReporterTheme {
        TitleBar(
            viewRoutes = ViewRoutes.Start,
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}