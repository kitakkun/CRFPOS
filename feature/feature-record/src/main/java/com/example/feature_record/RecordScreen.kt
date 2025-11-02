package com.example.feature_record

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    viewModel: RecordScreenViewModel,
    onClickBack: () -> Unit,
    onClickRecordItem: (recordId: Long) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contentResolver = LocalContext.current.contentResolver
    var pendingSaveTargetDate: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        launch {
            viewModel.exportToCsvSucceededFlow.collect {
                snackbarHostState.showSnackbar("CSVのエクスポートに成功しました")
            }
        }

        launch {
            viewModel.exportToCsvFailedFlow.collect {
                snackbarHostState.showSnackbar("CSVのエクスポートに失敗しました: $it")
            }
        }
    }

    val fileSavingTargetDirectoryPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            viewModel.exportRecordToCSV(
                date = pendingSaveTargetDate ?: return@rememberLauncherForActivityResult,
                outputTargetDirUri = uri,
                contentResolver = contentResolver,
            )
            pendingSaveTargetDate = null
        }

    RecordScreenView(
        displayMode = viewModel.displayMode,
        recordList = viewModel.rawRecords.collectAsState(emptyList()).value,
        recordDateList = viewModel.dateList.collectAsState(emptyList()).value,
        dailyGoodsSalesSummary = viewModel.dailyGoodsSalesSummary.collectAsState(emptyList()).value,
        snackbarHostState = snackbarHostState,
        onClickBack = onClickBack,
        onSelectDisplayMode = viewModel::updateDisplayMode,
        onClickRecordItem = { onClickRecordItem(it.id) },
        onClickSummarizedRecordItem = {
            pendingSaveTargetDate = it.date
            val documentDirUri =
                "content://com.android.externalstorage.documents/document/primary:Documents".toUri()
            fileSavingTargetDirectoryPickerLauncher.launch(documentDirUri)
        },
    )
}
