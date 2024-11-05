@file:OptIn(ExperimentalMaterialApi::class)

package com.develop.home.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.develop.common.constants.AppLanguage
import com.develop.home.ui.states.ArticlesState
import com.develop.home.ui.states.SettingsUiState
import com.develop.ui.R
import com.develop.ui.components.LifeCycleActions
import com.develop.ui.components.SettingsButton
import com.develop.ui.components.SheetScaffold
import com.develop.ui.components.SheetState
import com.develop.ui.components.rememberSheetScaffoldState
import com.develop.ui.dialogs.error.ErrorDialog
import com.develop.ui.theme.MaterialNewsTheme
import com.develop.ui.theme.isDarkTheme
import com.develop.ui.util.actions.CommonAction
import com.develop.ui.util.actions.UIActions
import com.develop.ui.util.actions.UIActionsImpl
import com.develop.ui.util.listRoundedCorner
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiActions: UIActions = UIActionsImpl(),
    onShowSnackBar:(String)->Unit = {},
    onNavToWebView:(link:String)->Unit = {},
    onNavToFilter:()->Unit = {},
    onNavBackStack:()->Unit = {},
    articlesSate:ArticlesState = ArticlesState(),
    settingsUiState: SettingsUiState = SettingsUiState(),
    onChangeLng:(AppLanguage)->Unit = {},
    onToggleDarkTheme:(Boolean)->Unit = {},
){
    if (uiActions.errorDialog.value != null){
        ErrorDialog(
            msg = uiActions.errorDialog.value?.msg?.asString() ?: "",
            title = uiActions.errorDialog.value?.title?.asString() ?: ""
        )
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val articles = articlesSate.articlesPaging.collectAsLazyPagingItems()

    val isRefreshing by remember {
        derivedStateOf { articles.loadState.refresh is LoadState.Loading }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { articles.refresh() },
        refreshingOffset = RefreshingOffset,
    )

    val state = rememberSheetScaffoldState(
        initPosition = SheetState.Collapsed,
        peekHeight = BackfoldHeight,
        toolbarHeight = ToolbarHeight,
        pullToRefreshState = pullRefreshState,
        refreshingOffset = RefreshingOffset
    )
    val lazyColumnState = rememberLazyListState()

    val onNavBack:()->Unit = remember {
        {
            if (state.currentValue == SheetState.Collapsed){
                onNavBackStack()
            }else{
                scope.launch { state.setState(SheetState.Collapsed) }
                scope.launch { lazyColumnState.scrollToItem(0) }
            }
        }
    }

    LifeCycleActions(uiActions.actions){ action ->
        when(action){
            CommonAction.NavBack -> onNavBack()
            is CommonAction.ShowSnackBar -> onShowSnackBar(action.msg.asString(context))
        }
    }

    BackHandler {
        onNavBack()
    }

    val systemPaddings = WindowInsets.systemBars.asPaddingValues()
    val bottomPaddings = systemPaddings.calculateBottomPadding()
    val navigationBarHeight:Dp by remember(bottomPaddings) {
        mutableStateOf(bottomPaddings)
    }

    val bottomSheetCorners by remember {
        derivedStateOf {
            val p1 = 1f - ((state.progress - 0.7f) / (1f - 0.7f)).coerceIn(0f, 1f)
            16.dp * p1
        }
    }

    val isDarkTheme = MaterialTheme.isDarkTheme
    val isDarkStatusBar by remember {
        derivedStateOf {
            state.progress >= 0.75f
        }
    }

    LaunchedEffect(key1 = isDarkStatusBar, key2 = isDarkTheme) {
        val activity = context as ComponentActivity
        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = if (isDarkTheme) false else isDarkStatusBar
        }
    }

    val drawToolbarShadow by remember {
        derivedStateOf { lazyColumnState.firstVisibleItemScrollOffset > 0 }
    }
    val toolbarElevation by animateDpAsState(
        targetValue = if (drawToolbarShadow) 4.dp else 0.dp,
        label = "toolbarElevation"
    )

    SheetScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.scrim)
            .fillMaxSize(),
        state = state,
        backfold = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.scrim)
                    .statusBarsPadding()
                    .padding(top = ScreenOffsets)
                    .height(BackfoldHeight)
                    .fillMaxWidth()
            ) {
                SettingsButtons(
                    modifier = Modifier
                        .padding(horizontal = ScreenOffsets)
                        .fillMaxWidth(),
                    state = settingsUiState,
                    onChangeLng = onChangeLng,
                    onToggleDarkTheme = onToggleDarkTheme
                )

                Spacer(modifier = Modifier.height(16.dp))

                TopHeadersPager(
                    state = articlesSate,
                    onClick = { onNavToWebView(it.url) }
                )
            }
        },
        sheet = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(
                    topStart = bottomSheetCorners,
                    topEnd = bottomSheetCorners
                ),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp + navigationBarHeight
                    )
                ) {
                    items(
                        count = articles.itemCount,
                        key = { index -> articles.peek(index)?.url ?: index }
                    ){ index ->
                        val item = articles[index]
                        NewsItem(
                            item = item,
                            itemPosition = listRoundedCorner(index, articles.itemCount),
                            onClick = { onNavToWebView(it.url) },
                        )
                    }
                }
            }
        },
        toolbar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shadowElevation = toolbarElevation
            ) {
                Row(
                    modifier = Modifier
                        .shadow(toolbarElevation)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(ToolbarHeight),
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
                        text = stringResource(id = R.string.home_screen_toolbar),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onNavToFilter) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_alt_24),
                            contentDescription = "filter",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        pullToRefreshIndicator = {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    )
}

@Composable
private fun SettingsButtons(
    modifier: Modifier = Modifier,
    state: SettingsUiState = SettingsUiState(),
    onChangeLng:(AppLanguage)->Unit = {},
    onToggleDarkTheme:(Boolean)->Unit = {},
){

    val localIsDarkTheme by state.isDarkTheme.collectAsStateWithLifecycle()
    val isDarkTheme = localIsDarkTheme ?: isSystemInDarkTheme()

    var showLngOptions:Boolean by remember {
        mutableStateOf(false)
    }
    val currentLng by state.currentLng.collectAsStateWithLifecycle()


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsButton(
            title = stringResource(id = R.string.home_screen_language_bts_title),
            iconRes = R.drawable.language_ic,
            haveOptions = true,
            modifier = Modifier
                .testTag(stringResource(id = R.string.home_screen_language_bts_title))
                .height(60.dp)
                .weight(1f),
            menu = {
                LanguagePopupOptions(
                    expanded = showLngOptions,
                    onDismissRequest = { showLngOptions = showLngOptions.not() },
                    onClick = onChangeLng,
                    currentLocale = currentLng ?: AppLanguage.EN
                )
            },
            onClick = { showLngOptions = true }
        )

        MaterialNewsTheme {
            SettingsButton(
                title = stringResource(id = R.string.home_screen_dark_theme_bts_title),
                iconRes = R.drawable.dark_theme_ic,
                isActive = isDarkTheme,
                onClick = {
                    onToggleDarkTheme(!isDarkTheme)
                },
                modifier = Modifier
                    .testTag(stringResource(id = R.string.home_screen_dark_theme_bts_title))
                    .height(60.dp)
                    .weight(1f)
            )
        }
    }
}

private val ToolbarHeight = 54.dp
internal val ScreenOffsets = 16.dp
private val BackfoldHeight = 244.dp
private val RefreshingOffset = 50.dp


@Preview
@Composable
private fun HomePreview(){
    MaterialNewsTheme {
        HomeScreen()
    }
}