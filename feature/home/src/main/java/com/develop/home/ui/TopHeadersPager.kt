package com.develop.home.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.develop.data.models.articles.ArticleModel
import com.develop.home.ui.states.TopHeaderState
import com.develop.ui.theme.White

@Composable
fun TopHeadersPager(
    modifier: Modifier = Modifier,
    state: TopHeaderState,
    onClick:(ArticleModel)->Unit,
){
    val items by state.items.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { items.size })

    Column(
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            key = { index -> items[index]?.url ?: index },
            contentPadding = PaddingValues(horizontal = ScreenOffsets),
            pageSpacing = 16.dp,
            beyondViewportPageCount = 2
        ) { index ->
            val item = items[index]

            HeaderNewsItemView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
                item = item,
                onClick = onClick
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .height(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val indicatorSize by animateDpAsState(
                    targetValue = if (pagerState.currentPage == iteration) 8.dp else 6.dp,
                    label = "indicatorSize"
                )

                val color =
                    if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else
                        White
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(indicatorSize)
                )
            }

        }
    }
}