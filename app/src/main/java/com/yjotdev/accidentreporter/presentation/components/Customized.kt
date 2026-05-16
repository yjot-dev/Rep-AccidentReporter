package com.yjotdev.accidentreporter.presentation.components

import android.content.res.Configuration
import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme

@Composable
fun ButtonAccidentReporter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun TextFieldAccidentReporter(
    modifier: Modifier = Modifier,
    @DimenRes idHeight: Int = R.dimen.dp_6,
    labelText: String,
    enabled: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit
){
    Box(
        modifier = Modifier
            .border(
                width = dimensionResource(R.dimen.dp_1),
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            )
            .height(dimensionResource(idHeight))
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterStart
    ){
        TextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            },
            colors = TextFieldDefaults.colors().copy(
                disabledContainerColor = MaterialTheme.colorScheme.background
            ),
            textStyle = MaterialTheme.typography.labelMedium,
            maxLines = 10
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun PreviewButtonAccidentReporter() {
    AccidentReporterTheme {
        ButtonAccidentReporter(
            onClick = {},
            text = "Button"
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
fun PreviewTextFieldAccidentReporter() {
    AccidentReporterTheme {
        TextFieldAccidentReporter(
            labelText = "Label",
            value = "",
            onValueChange = {}
        )
    }
}