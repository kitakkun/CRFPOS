package com.example.feature_record;

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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.dao.RecordDao
import com.example.model.Record

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreenView(
    displayMode: RecordScreenDisplayMode,
    recordList: List<Record>,
    recordDateList: List<RecordDao.Summary>,
    dailyGoodsSalesSummary: List<DailyGoodsSalesSummary>,
    snackbarHostState: SnackbarHostState,
    onClickBack: () -> Unit,
    onClickExportCSV: () -> Unit,
    onSelectDisplayMode: (RecordScreenDisplayMode) -> Unit,
    onClickRecordItem: (Record) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("売上管理") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    TextButton(
                        onClick = onClickExportCSV,
                    ) {
                        Text("Export CSV")
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
                currentDisplayMode = displayMode,
                onSelect = onSelectDisplayMode,
            )
            when (displayMode) {
                RecordScreenDisplayMode.PerRecord -> RawRecordScreenView(
                    recordList = recordList,
                    onClickItem = onClickRecordItem,
                )

                RecordScreenDisplayMode.PerDate -> SummarizeRecordScreenView(
                    recordDateList = recordDateList,
                )

                RecordScreenDisplayMode.PerDateGoods -> SummarizeGoodsScreenView(
                    dailyGoodsSalesSummary = dailyGoodsSalesSummary,
                )
            }
        }
    }
}