package com.yjotdev.accidentreporter.presentation.mvvm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.presentation.utils.ComponentPreview
import com.yjotdev.accidentreporter.presentation.utils.TestTags

@Composable
fun StartView(
    modifier: Modifier = Modifier,
    onTokenConfig: () -> Unit,
    onLocationConfig: () -> Unit
){
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.dp_3)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.startview_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Image(
            painter = painterResource(R.drawable.cono),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        ButtonAccidentReporter(
            onClick = onTokenConfig,
            text = stringResource(R.string.startview_button1),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag(TestTags.START_VIEW_BUTTON_1)
        )
        ButtonAccidentReporter(
            onClick = onLocationConfig,
            text = stringResource(R.string.startview_button2),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag(TestTags.START_VIEW_BUTTON_2)
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewStartView(){
    AccidentReporterTheme {
        StartView(
            modifier = Modifier.fillMaxSize(),
            onTokenConfig = {},
            onLocationConfig = {}
        )
    }
}