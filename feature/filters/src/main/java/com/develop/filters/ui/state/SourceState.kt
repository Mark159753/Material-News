package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import com.develop.data.models.spurces.SourceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
interface SourceState: ChangesComparator {

    val sources:StateFlow<List<SourceModel>>

    val selectedSources:StateFlow<Set<String>>

    fun onSelect(id:String)

}

class SourceStateImpl(
    initSources:Flow<List<SourceModel>>,
    initSelectedSources:Flow<Set<String>>,
    scope:CoroutineScope,
    private val onChange: OnChange,
):SourceState{

    override val sources: StateFlow<List<SourceModel>> = initSources
        .stateIn(
            scope = scope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private var _initSelectedSources:Set<String> = emptySet()

    private val _selectedSources:MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    override val selectedSources: StateFlow<Set<String>>
        get() = _selectedSources

    init {
        scope.launch {
            initSelectedSources.firstOrNull()?.takeIf { it.isNotEmpty() }?.let { sources ->
                _initSelectedSources = sources
                _selectedSources.value = sources
            }
        }
    }

    override fun onSelect(id: String) {
        if (_selectedSources.value.contains(id)){
            _selectedSources.value = _selectedSources.value.minusElement(id)
        }else{
            _selectedSources.value = _selectedSources.value.plusElement(id)
        }
        onChange.onChange()
    }

    override fun checkIsChanged() = _selectedSources.value != _initSelectedSources

    override fun isCorrect() = _selectedSources.value.isNotEmpty()
}