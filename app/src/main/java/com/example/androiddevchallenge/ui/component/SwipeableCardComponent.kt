package com.example.androiddevchallenge.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.data.TimerItemEntity
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SwipeableCardComponent(
    timerItem: TimerItemEntity,
    onClickDelete: () -> Unit,
    expandedBoxHeight: Dp,
    collapsedBoxHeight: Dp
) {
    val swipeableState = rememberSwipeableState(CardState.DEFAULT)
    val anchors: Map<Float, CardState> = mapOf(
        -200f to CardState.DELETE_LEFT,
        0f to CardState.DEFAULT,
        200f to CardState.DELETE_RIGHT
    )
    var expanded by remember { mutableStateOf(false)}
    val boxHeight by animateDpAsState(targetValue = if (expanded) expandedBoxHeight else collapsedBoxHeight)


    Box {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .padding(horizontal = 42.dp, vertical = 18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .clickable(onClick = { onClickDelete() }),
                    color = Color.Red
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteForever,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.size(45.dp).padding(start = 15.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = { onClickDelete() }),
                    color = Color.Red
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteForever,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.size(45.dp).padding(end = 15.dp)
                        )
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 12.dp,
            // backgroundColor = CardColors.valueOf(timerItem.color).color,
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .padding(horizontal = 40.dp, vertical = 16.dp)
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.clickable(
                    onClick = {
                        expanded = !expanded
                    }
                )
            ) {
                Text(
                    text = timerItem.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

enum class CardState {
    DELETE_LEFT,
    DELETE_RIGHT,
    DEFAULT,
}