package com.example.androiddevchallenge.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.resource.CardColors

@Composable
fun ColorPickerTileComponent(
    color: CardColors,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        elevation = 8.dp,
        shape = CircleShape,
        color = color.color
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClick(color.name) }
        ) {
            Text(
                text = color.colorName,
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}