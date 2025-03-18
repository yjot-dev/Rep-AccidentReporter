package com.yjotdev.accidentreporter.application.mvvm.view

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.composable.ButtonAccidentReporter
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme

@Composable
fun StartView(
    modifier: Modifier = Modifier,
    onNext: () -> Unit
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
            onClick = onNext,
            text = stringResource(R.string.startview_button),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag("startview_button")
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewStartView(){
    AccidentReporterTheme {
        StartView(
            modifier = Modifier.fillMaxSize(),
            onNext = {}
        )
    }
}