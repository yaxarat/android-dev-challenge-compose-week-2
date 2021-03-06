package com.example.androiddevchallenge.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun BottomSheetTitleComponent(
    expandedTitle: String,
    collapsedTitle: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(64.dp),
        contentAlignment = Alignment.Center
    ) {
        val title = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) expandedTitle else collapsedTitle

        Text(text = title, style = MaterialTheme.typography.button)
    }
}