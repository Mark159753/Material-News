package com.develop.ui.util.strings

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

@Immutable
sealed class UIText {
    data class DynamicString(
        val value:String
    ):UIText()

    @Immutable
    data class ResString(
        @StringRes
        val id:Int,
        val args:Array<Any> = arrayOf()
    ):UIText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ResString

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    @Composable
    @ReadOnlyComposable
    fun asString():String = when(this){
        is DynamicString -> value
        is ResString -> LocalContext.current.getString(id, *args)
    }

    fun asString(context: Context):String = when(this){
        is DynamicString -> value
        is ResString -> context.getString(id, *args)
    }
}