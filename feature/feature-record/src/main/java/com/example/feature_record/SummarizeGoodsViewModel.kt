package com.example.feature_record

import androidx.lifecycle.ViewModel
import com.example.data.repository.RecordRepository
import com.example.model.CartItem
import com.example.model.Goods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.map

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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return recordList.map { records ->
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
    }


}