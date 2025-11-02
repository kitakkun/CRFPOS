package com.example.feature_record

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    repository: RecordRepository,
) : ViewModel() {
    val items = repository.getAll()

    var displayMode: RecordScreenDisplayMode by mutableStateOf(RecordScreenDisplayMode.PerRecord)
        private set

    fun updateDisplayMode(mode: RecordScreenDisplayMode) {
        displayMode = mode
    }
}
