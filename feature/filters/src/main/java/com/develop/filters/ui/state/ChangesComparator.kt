package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ChangesComparator{

    fun checkIsChanged():Boolean

    fun isCorrect():Boolean = true
}

fun interface OnChange{
    fun onChange()
}

@Stable
interface ChangeState{

    val anyChanges: StateFlow<Boolean>

    fun checkHaveAnyChanges(vararg comparator: ChangesComparator)
}

class ChangeStateImpl:ChangeState{

    private val _anyChanges = MutableStateFlow(false)
    override val anyChanges: StateFlow<Boolean>
        get() = _anyChanges

    override fun checkHaveAnyChanges(vararg comparator: ChangesComparator) {
        val isCorrect = comparator.all { it.isCorrect() }
        _anyChanges.value = if (isCorrect) comparator.any { it.checkIsChanged() } else false
    }
}