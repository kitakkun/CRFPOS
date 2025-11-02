package com.example.feature_record

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class RecordScreenDisplayMode(
    val labelText: String,
) {
    PerRecord("レコード別"),
    PerDate("日別"),
    PerDateGoods("日別商品別"),
}

@Composable
fun SegmentedHistoryDisplayModeButtons(
    currentDisplayMode: RecordScreenDisplayMode,
    onSelect: (RecordScreenDisplayMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.widthIn(min = 400.dp),
    ) {
        RecordScreenDisplayMode.entries.forEachIndexed { index, mode ->
            SegmentedButton(
                selected = currentDisplayMode == mode,
                onClick = { onSelect(mode) },
                shape = when (index) {
                    0 -> RoundedCornerShape(topStartPercent = 100, bottomStartPercent = 100)
                    RecordScreenDisplayMode.entries.lastIndex -> RoundedCornerShape(
                        topEndPercent = 100,
                        bottomEndPercent = 100
                    )

                    else -> RectangleShape
                },
                label = { Text(mode.labelText) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedHistoryDisplayModeButtonsPreview() {
    var selectedMode by remember { mutableStateOf(RecordScreenDisplayMode.PerRecord) }
    SegmentedHistoryDisplayModeButtons(
        currentDisplayMode = selectedMode,
        onSelect = { selectedMode = it },
    )
}
