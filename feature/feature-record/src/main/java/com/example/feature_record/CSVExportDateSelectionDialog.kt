package com.example.feature_record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CSVExportDateSelectionDialog(
    exportableDates: List<String>,
    onDismissRequest: () -> Unit,
    onClickCancel: () -> Unit,
    onClickExport: (selectedDates: Set<String>) -> Unit,
) {
    val selectedDates: SnapshotStateMap<String, Boolean> = remember {
        mutableStateMapOf(
            *exportableDates.associateWith { false }.toList().toTypedArray()
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium,
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "CSV Export", style = MaterialTheme.typography.headlineSmall)
            Text(text = "エクスポートする日付:")
            exportableDates.forEach { date ->
                ExportTargetDateItem(
                    label = date,
                    checked = selectedDates[date] ?: false,
                    onCheckedChange = { isChecked -> selectedDates[date] = isChecked },
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(onClick = onClickCancel) {
                    Text(text = "キャンセル")
                }
                Button(
                    onClick = {
                        onClickExport(selectedDates.keys.toSet())
                    }
                ) {
                    Text(text = "エクスポート")
                }
            }
        }
    }
}

@Composable
private fun ExportTargetDateItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            onClick = { onCheckedChange(!checked) },
        )
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        Text(text = label)
    }
}

@Preview
@Composable
private fun CSVExportDateSelectionDialogPreview() {
    CSVExportDateSelectionDialog(
        exportableDates = listOf(
            "2024-01-01",
            "2024-01-02",
            "2024-01-03",
        ),
        onDismissRequest = {},
        onClickExport = {},
        onClickCancel = {},
    )
}