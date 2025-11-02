package com.example.feature_record

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreenRoot(
    viewModel: RecordScreenRootViewModel,
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("売上管理") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SegmentedHistoryDisplayModeButtons(
                currentDisplayMode = viewModel.displayMode,
                onSelect = viewModel::updateDisplayMode,
            )
            when (viewModel.displayMode) {
                RecordScreenDisplayMode.PerRecord -> RecordScreen(
                    recordList = viewModel.rawRecords.collectAsState(emptyList()).value,
                    onClickItem = { onClickRecordItem(it.id) },
                )

                RecordScreenDisplayMode.PerDate -> SummarizeRecordScreen(
                    recordDateList = viewModel.dateList.collectAsState(emptyList()).value,
                    onClickItem = {
                        pendingSaveTargetDate = it.date
                        val documentDirUri =
                            "content://com.android.externalstorage.documents/document/primary:Documents".toUri()
                        fileSavingTargetDirectoryPickerLauncher.launch(documentDirUri)
                    },
                )

                RecordScreenDisplayMode.PerDateGoods -> SummarizeGoodsScreen(
                    dailyGoodsSalesSummary = viewModel.dailyGoodsSalesSummary.collectAsState(
                        emptyList()
                    ).value
                )
            }
        }
    }
}
