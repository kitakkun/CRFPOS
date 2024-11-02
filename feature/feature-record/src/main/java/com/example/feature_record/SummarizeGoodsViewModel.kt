package com.example.feature_record

import androidx.lifecycle.ViewModel
import com.example.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SummarizeGoodsViewModel @Inject constructor(
    private val recordRepository: RecordRepository,
) : ViewModel() {
    private val recordList = recordRepository.getAll()

    val dailyGoodsSalesSummary: Flow<List<DailyGoodsSalesSummary>> = getDailySalesSummary()

    data class DailyGoodsSalesSummary(
        val date: String,
        val salesSummary: List<GoodsSalesSummary>
    )

    data class GoodsSalesSummary(
        val goodsId: Long,
        val goodsName: String,
        var totalQuantity: Int = 0
    )

    private fun getDailySalesSummary(): Flow<List<DailyGoodsSalesSummary>> {
        return recordList.map { records ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dailySummaryMap = mutableMapOf<String, MutableMap<Long, GoodsSalesSummary>>()

            records.forEach { record ->
                // 日付を文字列として取得（例: "2024-10-30"）
                val dateKey = dateFormat.format(Date(record.time))

                // 日付ごとの集計を格納するマップを取得
                val summaryMap = dailySummaryMap.getOrPut(dateKey) { mutableMapOf() }

                record.goodsList?.forEach { cartItem ->
                    val goods = cartItem.goods
                    val quantity = cartItem.quantity

                    // 商品ごとの集計を更新
                    val summary = summaryMap.getOrPut(goods.displayOrder.toLong()) {
                        GoodsSalesSummary(goodsId = goods.displayOrder.toLong(), goodsName = goods.name)
                    }
                    summary.totalQuantity += quantity
                }
            }

            // マップからリストに変換し、商品ID順に並び替え
            dailySummaryMap.map { (date, salesMap) ->
                DailyGoodsSalesSummary(
                    date = date,
                    salesSummary = salesMap.values.sortedBy { it.goodsId }  // 商品ID順にソート
                )
            }
        }
    }


}