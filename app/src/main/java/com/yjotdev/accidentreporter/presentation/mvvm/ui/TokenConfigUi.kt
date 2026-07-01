package com.yjotdev.accidentreporter.presentation.mvvm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.presentation.components.TextFieldAccidentReporter
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.presentation.utils.ComponentPreview
import com.yjotdev.accidentreporter.presentation.utils.TestTags

@Composable
fun TokenConfigView(
    modifier: Modifier = Modifier,
    tokenText: String,
    onTokenText: (String) -> Unit,
    enableControls: Boolean,
    onEnableControls: (Boolean) -> Unit,
    onUpdate: () -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        TextFieldAccidentReporter(
            enabled = enableControls,
            idHeight = R.dimen.dp_5,
            labelText = stringResource(R.string.textfield_token),
            value = tokenText,
            onValueChange = { onTokenText(it) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag(TestTags.TOKEN_CONFIG_VIEW_TEXT_FIELD)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = TestTags.TOKEN_CONFIG_VIEW_EDIT_BUTTON,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_5))
                    .clickable{
                        onEnableControls(enableControls)
                    }
            )
            ButtonAccidentReporter(
                onClick = onUpdate,
                text = stringResource(R.string.tokenconfigview_button),
                enabled = enableControls,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(dimensionResource(R.dimen.dp_5))
                    .testTag(TestTags.TOKEN_CONFIG_VIEW_UPDATE_BUTTON)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewTokenConfigView(){
    AccidentReporterTheme {
        TokenConfigView(
            modifier = Modifier.fillMaxSize(),
            tokenText = "123456789",
            onTokenText = {},
            enableControls = true,
            onEnableControls = {},
            onUpdate = {}
        )
    }
}