package com.yjotdev.accidentreporter.application.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.yjotdev.accidentreporter.R

@Composable
fun ComboBoxAccidentReporter(
    modifier: Modifier = Modifier,
    optionList: List<String>,
    enabled: Boolean = true,
    indexSelected: Int,
    onIndexSelected: (Int) -> Unit
){
    var showOptions by remember { mutableStateOf(false) }
    val colorList = listOf(
        MaterialTheme.colorScheme.background,
        colorResource(R.color.green),
        colorResource(R.color.red),
        colorResource(R.color.yellow),
    )
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(0.8f)
            .border(
                width = dimensionResource(R.dimen.dp_1),
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            ).padding(dimensionResource(R.dimen.dp_2))
    ){
        item{
            ComboBoxItem(
                textItem = optionList[indexSelected],
                textColor = colorList[indexSelected],
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
                    .alpha(if(enabled) 1f else 0.5f)
                    .clickable(enabled = enabled) {
                        showOptions = !showOptions
                        onIndexSelected(indexSelected)
                    }
            )
        }
        items(count = optionList.size){ index ->
            AnimatedVisibility(
                visible = showOptions,
                enter = expandVertically(spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )),
                exit = shrinkVertically(spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ))
            ) {
                ComboBoxItem(
                    textItem = optionList[index],
                    textColor = colorList[index],
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            showOptions = !showOptions
                            onIndexSelected(index)
                        }.testTag("combobox_item:${index + 1}")
                )
            }
        }
    }
}

@Composable
private fun ComboBoxItem(
    modifier: Modifier = Modifier,
    textItem: String,
    textColor: Color,
    textAlign: TextAlign = TextAlign.Start
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier.size(dimensionResource(R.dimen.dp_3))
                .background(
                    color = textColor,
                    shape = MaterialTheme.shapes.extraLarge
                )
        )
        Text(
            text = textItem,
            style = MaterialTheme.typography.labelMedium.copy(
                textAlign = textAlign
            ),
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.fillMaxWidth()
                .padding(start = dimensionResource(R.dimen.dp_3))
        )
    }
}