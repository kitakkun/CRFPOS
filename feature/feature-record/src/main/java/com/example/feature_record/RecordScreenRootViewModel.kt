package com.example.feature_record

import android.content.Context
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordScreenRootViewModel @Inject constructor(
    private val repository: RecordRepository,
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

    fun updateDisplayMode(mode: RecordScreenDisplayMode) {
        displayMode = mode
    }

    fun exportRecordToCSV(date: String, context: Context?) {
        viewModelScope.launch {
            try {
                val recordList = repository.getDiaryData(date)
                val exporter = Exporter()
                exporter.writeRecordsToCSV(recordList, date, context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}