package com.develop.filters.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.develop.data.models.filters.SortBy
import com.develop.filters.R

@Composable
fun SortBySection(
    modifier: Modifier = Modifier,
    selected: SortBy,
    onSetSortOrder:(SortBy)->Unit = {}
){

    val entries = remember {
        SortBy.entries
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.filter_screen_sort_order_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(100)
                )
                .fillMaxWidth()
                .height(40.dp)
        ) {
            entries.forEachIndexed { index, item ->
                SortItemView(
                    item = item,
                    isSelected = item == selected,
                    modifier = Modifier
                        .semantics { contentDescription = item.name }
                        .fillMaxHeight()
                        .weight(1f),
                    onClick = onSetSortOrder
                )
                if (index != entries.size - 1){
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun SortItemView(
    modifier: Modifier = Modifier,
    item:SortBy,
    isSelected:Boolean = false,
    onClick:(SortBy)->Unit = {}
){

    val bgColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.secondaryContainer
        else Color.Transparent
    )

    val titleColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.onSurface
    )

    Row(
        modifier = modifier
            .clickable { onClick(item) }
            .background(bgColor)
            .padding(
                horizontal = 12.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        AnimatedVisibility(visible = isSelected) {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
        }

        Text(
            text = item.getUiText(),
            style = MaterialTheme.typography.labelMedium,
            color = titleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@ReadOnlyComposable
private fun SortBy.getUiText():String = when(this){
    SortBy.POPULARITY -> stringResource(id = R.string.filter_screen_sort_by_popularity)
    SortBy.RELEVANCY -> stringResource(id = R.string.filter_screen_sort_by_relevancy)
    SortBy.PUBLISHED_AT -> stringResource(id = R.string.filter_screen_sort_by_published_at)
}