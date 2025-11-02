package com.example.feature_record

import android.content.ContentResolver
import android.net.Uri
import android.provider.DocumentsContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RecordRepository
import com.example.model.CartItem
import com.example.model.Goods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordScreenViewModel @Inject constructor(
    private val repository: RecordRepository,
    private val recordsCSVStringGenerator: RecordsCSVStringGenerator,
) : ViewModel() {
    val rawRecords = repository.getAll()

    val dailyGoodsSalesSummary: Flow<List<DailyGoodsSalesSummary>> = rawRecords.map { records ->
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        records.groupBy { record ->
            val dateKey = dateFormat.format(Date(record.time))
            dateKey
        }.map { (dateKey, recordsForDate) ->
            val allCartItemsForDate: List<CartItem> =
                recordsForDate.flatMap { it.goodsList.orEmpty() }
            val goodsCartItemsMap: Map<Goods, List<CartItem>> =
                allCartItemsForDate.groupBy { it.goods }

            DailyGoodsSalesSummary(
                date = dateKey,
                salesSummary = goodsCartItemsMap.map { (goods, cartItems) ->
                    GoodsSalesSummary(
                        totalQuantity = cartItems.sumOf { it.quantity },
                        goodsId = goods.id,
                        goodsName = goods.name,
                    )
                }
            )
        }
    }

    val dateList = repository.getDateList()

    var displayMode: RecordScreenDisplayMode by mutableStateOf(RecordScreenDisplayMode.PerRecord)

    private val mutableExportToCsvSucceededFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    val exportToCsvSucceededFlow: SharedFlow<Unit> = mutableExportToCsvSucceededFlow

    private val mutableExportToCsvFailedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val exportToCsvFailedFlow: SharedFlow<String> = mutableExportToCsvFailedFlow

    fun updateDisplayMode(mode: RecordScreenDisplayMode) {
        displayMode = mode
    }

    fun exportRecordToCSV(
        exportTargetDates: Set<String>,
        outputTargetDirUri: Uri,
        contentResolver: ContentResolver,
    ) {
        viewModelScope.launch {
            try {
                exportTargetDates.forEach { exportTargetDate ->
                    val recordList = repository.getDiaryData(exportTargetDate)
                    val parentDocumentUri = DocumentsContract.buildDocumentUriUsingTree(
                        outputTargetDirUri,
                        DocumentsContract.getTreeDocumentId(outputTargetDirUri)
                    )

                    val document = DocumentsContract.createDocument(
                        contentResolver,
                        parentDocumentUri,
                        "text/csv",
                        "$exportTargetDate.csv"
                    ) ?: return@launch

                    val outputTextContent =
                        recordsCSVStringGenerator.generateFromRecords(recordList)

                    contentResolver.openOutputStream(document)?.use { output ->
                        output.write(outputTextContent.toByteArray())
                    }
                }

                mutableExportToCsvSucceededFlow.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                mutableExportToCsvFailedFlow.emit("エクスポートに失敗しました: ${e.message}")
            }
        }
    }
}