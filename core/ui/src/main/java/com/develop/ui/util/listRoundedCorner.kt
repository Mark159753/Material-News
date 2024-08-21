package com.develop.ui.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun listRoundedCorner(index:Int, size:Int) = when{
    size <= 1 -> ItemPosition.Single
    index == 0 -> ItemPosition.First
    index == (size - 1) -> ItemPosition.Last
    else -> ItemPosition.Middle
}

enum class ItemPosition{
    Single,
    First,
    Last,
    Middle
}

fun getShapeByItemPosition(position: ItemPosition, corner:Dp) = when(position){
    ItemPosition.Single -> RoundedCornerShape(corner)
    ItemPosition.First -> RoundedCornerShape(topStart = corner, topEnd = corner)
    ItemPosition.Last -> RoundedCornerShape(bottomEnd = corner, bottomStart = corner)
    ItemPosition.Middle -> RoundedCornerShape(0.dp)
}