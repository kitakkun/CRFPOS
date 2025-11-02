package com.example.feature_record

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateSet
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
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val pendingSaveTargetDates: SnapshotStateSet<String> = remember { mutableStateSetOf() }

    val dateList by viewModel.dateList.collectAsState(emptyList())
    var showExportTargetDateSelectionDialog by remember { mutableStateOf(false) }

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
                exportTargetDates = pendingSaveTargetDates,
                outputTargetDirUri = uri,
                contentResolver = context.contentResolver,
            )
            showExportTargetDateSelectionDialog = false
        }

    if (showExportTargetDateSelectionDialog) {
        CSVExportDateSelectionDialog(
            exportableDates = dateList.map { it.date },
            onDismissRequest = { showExportTargetDateSelectionDialog = false },
            onClickCancel = { showExportTargetDateSelectionDialog = false },
            onClickExport = { selectedDates ->
                pendingSaveTargetDates.addAll(selectedDates)
                val documentDirUri =
                    "content://com.android.externalstorage.documents/document/primary:Documents".toUri()
                fileSavingTargetDirectoryPickerLauncher.launch(documentDirUri)
                Toast.makeText(
                    context,
                    "エクスポート先のディレクトリを選択してください",
                    Toast.LENGTH_SHORT,
                ).show()
            },
        )
    }

    RecordScreenView(
        displayMode = viewModel.displayMode,
        recordList = viewModel.rawRecords.collectAsState(emptyList()).value,
        recordDateList = dateList,
        dailyGoodsSalesSummary = viewModel.dailyGoodsSalesSummary.collectAsState(emptyList()).value,
        snackbarHostState = snackbarHostState,
        onClickBack = onClickBack,
        onSelectDisplayMode = viewModel::updateDisplayMode,
        onClickRecordItem = { onClickRecordItem(it.id) },
        onClickExportCSV = { showExportTargetDateSelectionDialog = true },
    )
}
