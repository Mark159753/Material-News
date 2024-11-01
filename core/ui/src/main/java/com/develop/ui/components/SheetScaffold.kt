@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)

package com.develop.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.ui.unit.Velocity
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class SheetState{
    Expanded, Collapsed
}

@Stable
class SheetScaffoldState(
    private val peekHeight:Dp = 200.dp,
    internal val toolbarHeight: Dp = 54.dp,
    private val initPosition:SheetState = SheetState.Collapsed,
    internal val density: Density,
    confirmValueChanged:(SheetState)->Boolean = { true },
    internal val pullToRefreshState: PullRefreshState?,
    private val refreshingOffset:Dp = 0.dp,
    private val scope: CoroutineScope,
){


    private val refreshingOffsetPx:Float
        get() = with(density){ refreshingOffset.toPx() }

    internal val pullOffset = Animatable(0f)

    private var statusBarPadding:Dp = 0.dp

    private val toolbarHeightPx:Float
        get()= with(density){ (toolbarHeight + statusBarPadding).toPx() }

    @OptIn(ExperimentalFoundationApi::class)
    internal val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initPosition,
        positionalThreshold = { distance:Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        confirmValueChange = confirmValueChanged,
        decayAnimationSpec = exponentialDecay(),
        snapAnimationSpec = tween()
        )

    internal fun updateAnchors(statusBarPadding:Dp){
        this.statusBarPadding = statusBarPadding
        anchoredDraggableState.updateAnchors(
            DraggableAnchors {
                SheetState.Collapsed at with(density) { (peekHeight + statusBarPadding).toPx() }
                SheetState.Expanded at with(density) {  (toolbarHeight + statusBarPadding).toPx() }
            }
        )
    }

    init {
        calculatePullOffset()
    }

    /*
    * if current state is SheetState.Collapsed progress will be 0f
    * if current state is SheetState.Expanded progress will be 1f
    * */
    val progress: Float
        get() = anchoredDraggableState.progress(SheetState.Collapsed, SheetState.Expanded)


    private val toolbarOffset: Float by derivedStateOf {
        val p1 = ((progress - 0.7f) / (1f - 0.7f)).coerceIn(0f, 1f)
        -(toolbarHeightPx * (1f - p1))
    }

    fun requireToolbarOffset(): Float {
        return toolbarOffset
    }


    val currentValue:SheetState
        get() = anchoredDraggableState.currentValue

    val targetValue:SheetState
        get() = anchoredDraggableState.targetValue


    suspend fun setState(state: SheetState){
        anchoredDraggableState.animateTo(state)
    }

    private fun calculatePullOffset(){
        if (pullToRefreshState == null) return
        scope.launch {
            snapshotFlow { pullToRefreshState.progress }.collectLatest { p ->
                val p1 = p.coerceIn(0f, 1f)
                if (p1 == 0f){
                    pullOffset.animateTo(p1)
                }else{
                    pullOffset.snapTo(p1 * refreshingOffsetPx)
                }
            }
        }
    }

    companion object {
        fun Saver(
            peekHeight:Dp = 200.dp,
            confirmValueChange: (SheetState) -> Boolean,
            density: Density,
            toolbarHeight: Dp = 54.dp,
            pullToRefreshState: PullRefreshState?,
            refreshingOffset:Dp,
            scope: CoroutineScope
        ) =
            Saver<SheetScaffoldState, SheetState>(
                save = { it.currentValue },
                restore = { savedValue ->
                    SheetScaffoldState(
                        peekHeight = peekHeight,
                        toolbarHeight = toolbarHeight,
                        initPosition = savedValue,
                        density = density,
                        confirmValueChanged = confirmValueChange,
                        pullToRefreshState = pullToRefreshState,
                        refreshingOffset = refreshingOffset,
                        scope = scope
                    )
                }
            )
    }
}


@Composable
fun rememberSheetScaffoldState(
    peekHeight:Dp = 200.dp,
    toolbarHeight: Dp = 54.dp,
    initPosition:SheetState = SheetState.Collapsed,
    confirmValueChanged:(SheetState)->Boolean = { true },
    pullToRefreshState: PullRefreshState? = null,
    density:Density = LocalDensity.current,
    refreshingOffset:Dp = 0.dp,
    scope: CoroutineScope = rememberCoroutineScope(),
): SheetScaffoldState = rememberSaveable(
    saver = SheetScaffoldState.Saver(
        peekHeight = peekHeight,
        confirmValueChange = confirmValueChanged,
        density = density,
        toolbarHeight = toolbarHeight,
        pullToRefreshState = pullToRefreshState,
        refreshingOffset = refreshingOffset,
        scope = scope,
    )
) {
    SheetScaffoldState(
        peekHeight = peekHeight,
        initPosition = initPosition,
        density = density,
        confirmValueChanged = confirmValueChanged,
        toolbarHeight = toolbarHeight,
        pullToRefreshState = pullToRefreshState,
        refreshingOffset = refreshingOffset,
        scope = scope
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetScaffold(
    modifier: Modifier = Modifier,
    state: SheetScaffoldState = rememberSheetScaffoldState(),
    backfold: @Composable ()->Unit,
    sheet:@Composable ()->Unit,
    toolbar:@Composable ()->Unit,
    pullToRefreshIndicator: (@Composable BoxWithConstraintsScope.()->Unit)? = null
){

    val systemPaddings = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val statusBarHeight:Dp by remember(systemPaddings) {
        mutableStateOf(systemPaddings)
    }

    LaunchedEffect(key1 = statusBarHeight) {
        state.updateAnchors(statusBarHeight)
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
        ){
        backfold()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight - state.toolbarHeight - statusBarHeight)
                .then(
                    if (state.pullToRefreshState != null)
                        Modifier.pullRefresh(state.pullToRefreshState)
                    else Modifier
                )
                .nestedScroll(rememberNestedScrollConnection(state))
                .offset {
                    IntOffset(
                        x = 0,
                        y = state.anchoredDraggableState
                            .offset
                            .takeIf { !it.isNaN() }
                            ?.roundToInt() ?: 0,
                    )
                }
                .offset {
                    IntOffset(
                        x = 0,
                        y = state.pullOffset.value.roundToInt()
                    )
                }
                .anchoredDraggable(
                    state.anchoredDraggableState,
                    Orientation.Vertical
                )
        ){
            sheet()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset {
                    IntOffset(
                        x = 0,
                        y = state
                            .requireToolbarOffset()
                            .roundToInt(),
                    )
                }
        ) {
            toolbar()
        }

        pullToRefreshIndicator?.invoke(this)
    }

}


@Composable
private fun rememberNestedScrollConnection(
    state:SheetScaffoldState,
): NestedScrollConnection {

    return remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return if (delta < 0 && source == NestedScrollSource.Drag) {
                    state.anchoredDraggableState.dispatchRawDelta(delta).toOffset()
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return if (source == NestedScrollSource.Drag) {
                    state.anchoredDraggableState.dispatchRawDelta(delta).toOffset()
                } else {
                    Offset.Zero
                }
            }


            override suspend fun onPreFling(available: Velocity): Velocity {
                val toFling = available.y
                val currentOffset = state.anchoredDraggableState.requireOffset()
                val minAnchor = state.anchoredDraggableState.anchors.minAnchor()
                return if (toFling < 0 && currentOffset > minAnchor) {
                    state.anchoredDraggableState.settle(toFling)
                    available
                } else {
                    Velocity.Zero
                }
            }


            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                state.anchoredDraggableState.settle(available.y)
                return available
            }
        }
    }
}

private fun Float.toOffset() = Offset(x = 0f, y = this)