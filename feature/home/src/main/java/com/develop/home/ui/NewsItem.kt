package com.develop.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import com.develop.common.date.relativeFormatDate
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.articles.generateFakeArticleModel
import com.develop.ui.components.news_img.getImageOrPreview
import com.develop.ui.theme.MaterialNewsTheme
import com.develop.ui.util.ItemPosition
import com.develop.ui.util.getShapeByItemPosition

@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    item:ArticleModel?,
    onClick:(ArticleModel)->Unit = {},
    itemPosition: ItemPosition = ItemPosition.Middle
){
    if (item == null) return

    val cardShape = remember(itemPosition) {
        getShapeByItemPosition(itemPosition, 16.dp)
    }

    Card(
        modifier = modifier,
        onClick = { if (item != null) onClick(item) },
        shape = cardShape,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AsyncImage(
                model = getImageOrPreview(
                    img = item.urlToImage,
                    source = item.url
                ),
                contentDescription = item.title,
                imageLoader = LocalContext.current.imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.outline),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = item.title ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = item.publishedAt?.relativeFormatDate() ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 6.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Text(
                    text = item.description ?: "",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall
                )

            }
        }
        if (itemPosition != ItemPosition.Last) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
            )
        }
    }
}


@Preview
@Composable
private fun NewsItemPreview(){
    MaterialNewsTheme {
        NewsItem(
            item = generateFakeArticleModel()
        )
    }
}