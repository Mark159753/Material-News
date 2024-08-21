package com.develop.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
internal fun HeaderNewsItemView(
    modifier: Modifier = Modifier,
    item:ArticleModel?,
    onClick:(ArticleModel)->Unit = {},
){
    if (item == null) return

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        onClick = { onClick(item) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
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
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.outline)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = item.title ?: "",
                    maxLines = 3,
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
        }
    }
}

@Preview
@Composable
private fun HeaderNewsItemViewPreview(){
    MaterialNewsTheme {
        HeaderNewsItemView(
            item = generateFakeArticleModel()
        )
    }
}