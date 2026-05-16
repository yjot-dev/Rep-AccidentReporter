package com.yjotdev.accidentreporter.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme

@Composable
fun Position(
    modifier: Modifier = Modifier,
    date: String,
    type: String,
    enabledDelete: Boolean,
    onToLook: () -> Unit,
    onDelete: () -> Unit,
){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(dimensionResource(R.dimen.dp_2))
    ) {
        Text(
            text = stringResource(R.string.mapview_date, date),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = stringResource(R.string.mapview_type, type),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(R.drawable.look),
                contentDescription = "mapview_lookbutton",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_5))
                    .clickable { onToLook() }
            )
            Image(
                painter = painterResource(R.drawable.delete),
                contentDescription = "mapview_deletebutton",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_5))
                    .alpha(if (enabledDelete) 1f else 0.5f)
                    .clickable(enabled = enabledDelete) { onDelete() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 400,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewPosition(){
    AccidentReporterTheme {
        BasicAlertDialog(
            onDismissRequest = {},
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .border(
                    width = dimensionResource(R.dimen.dp_1),
                    color = MaterialTheme.colorScheme.secondary,
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            Position(
                modifier = Modifier.fillMaxWidth(0.7f),
                date = "03/02/2025 10:10:23",
                type = "1: Accidentes",
                enabledDelete = true,
                onToLook = {},
                onDelete = {}
            )
        }
    }
}