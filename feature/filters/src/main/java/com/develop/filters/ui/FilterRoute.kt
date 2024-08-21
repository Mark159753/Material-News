package com.develop.filters.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.develop.filters.R
import com.develop.filters.ui.state.FilterState
import com.develop.ui.components.LifeCycleActions
import com.develop.ui.dialogs.error.ErrorDialog
import com.develop.ui.util.actions.CommonAction

@Composable
fun FilterRoute(
    viewModel: FilterViewModel = hiltViewModel(),
    onShowSnackBar:(String)->Unit,
    onNavBack: () -> Unit
){

    if (viewModel.errorDialog.value != null){
        ErrorDialog(
            msg = viewModel.errorDialog.value?.msg?.asString() ?: "",
            title = viewModel.errorDialog.value?.title?.asString() ?: ""
        )
    }

    val context = LocalContext.current

    LifeCycleActions(viewModel.actions){ action ->
        when(action){
            CommonAction.NavBack -> onNavBack()
            is CommonAction.ShowSnackBar -> onShowSnackBar(action.msg.asString(context))
        }
    }

    FilterScreen(
        onNavBack = onNavBack,
        onSave = viewModel::onSaveChanges,
        state = viewModel.state
    )
}

@Composable
private fun FilterScreen(
    onNavBack:()->Unit = {},
    onSave:()->Unit = {},
    state: FilterState,
){

    val isChanged by state.anyChanges.collectAsStateWithLifecycle()
    val sourcesList by state.sourceState.sources.collectAsStateWithLifecycle()

    val systemPaddings = WindowInsets.systemBars.asPaddingValues()
    val bottomPaddings = systemPaddings.calculateBottomPadding()

    val selectedSources by state.sourceState.selectedSources.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .statusBarsPadding()
                .fillMaxWidth()
                .height(54.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onNavBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.filter_screen_toolbar),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onSave,
                enabled = isChanged
            ) {
                Text(text = stringResource(id = R.string.filter_screen_save_btn))
            }

            Spacer(modifier = Modifier.width(12.dp))
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = bottomPaddings + 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    SortBySection(
                        state = state.sortByState
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.filter_screen_sources_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(
                sourcesList.size,
                key = { index -> sourcesList[index].id }
            ){ index ->
                val item = sourcesList[index]
                SourceItem(
                    item = item,
                    isSelected = selectedSources.contains(item.id),
                    onClick = { selected ->
                        state.sourceState.onSelect(selected.id)
                    }
                )
            }
        }

    }
}
