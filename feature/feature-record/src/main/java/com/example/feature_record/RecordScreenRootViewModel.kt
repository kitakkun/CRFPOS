package com.example.feature_record

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RecordScreenRootViewModel: ViewModel() {
    var displayMode: RecordScreenDisplayMode by mutableStateOf(RecordScreenDisplayMode.PerRecord)

    fun updateDisplayMode(mode: RecordScreenDisplayMode) {
        displayMode = mode
    }
}