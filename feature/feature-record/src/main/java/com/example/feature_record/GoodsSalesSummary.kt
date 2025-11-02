package com.example.feature_record

data class GoodsSalesSummary(
    val goodsId: Long,
    val goodsName: String,
    var totalQuantity: Int = 0
)
